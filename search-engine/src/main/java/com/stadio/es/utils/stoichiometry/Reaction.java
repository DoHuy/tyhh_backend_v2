package com.stadio.es.utils.stoichiometry;

import com.stadio.es.utils.numbers.Fraction;
import com.stadio.es.utils.numbers.Solver;
import com.stadio.es.utils.output.StringTable.Align;

import javax.management.openmbean.InvalidKeyException;
import java.util.*;
import java.util.Map.Entry;

public class Reaction {
    private final Expression reactants, products;

    public Reaction(Expression reactants, Expression products) {
        this.reactants = reactants;
        this.products = products;
    }

    public static Reaction fromString(String reaction) {

        String[] leftRight = reaction.split("->|=>|<-|<=|<|>|<<|>>|<>|<->|<=>|=|←|→|↔|«|»|⇆");


        Expression reactants = Expression.fromString(leftRight[0]);
        Expression products = Expression.fromString(leftRight[1]);

        //		System.out.println("Unsolved	" + reactants + " => " + products);

        Map<Compound, Integer> all = new LinkedHashMap<>();
        all.putAll(reactants.getCompounds());
        all.putAll(products.getCompounds());

        Set<Element> elements = new HashSet<>();
        for (Entry<Compound, Integer> entry : all.entrySet()) {
            Compound compound = entry.getKey();
            for (Entry<Element, Integer> entry1 : compound.getElements().entrySet()) {
                elements.add(entry1.getKey());
            }
        }

        int extraRows = 1;

        for (Entry<Compound, Integer> e : reactants.getCompounds().entrySet()) {
            if (e.getKey().getCharge() != 0) extraRows = 2;
        }
        for (Entry<Compound, Integer> e : products.getCompounds().entrySet()) {
            if (e.getKey().getCharge() != 0) extraRows = 2;
        }

        Fraction[][] mat = Fraction.new2DArray(extraRows + elements.size(), reactants.size() + products.size());
        mat[0][0] = Fraction.integer(1);

        int n = 0;

        if (extraRows == 2) {
            for (Entry<Compound, Integer> e : reactants.getCompounds().entrySet()) {
                mat[1][n] = Fraction.integer(e.getKey().getCharge());
                n++;
            }
            for (Entry<Compound, Integer> e : products.getCompounds().entrySet()) {
                mat[1][n] = Fraction.integer(-e.getKey().getCharge());
                n++;
            }
        }

        n = 0;
        //		System.out.println("[1, 0, ... 0] = [1]");
        for (Element e : elements) {
            List<Integer> factors = reactants.countFactors(e);
            factors.addAll(negative(products.countFactors(e)));
            for (int j = 0; j < factors.size(); j++) {
                mat[n + extraRows][j] = Fraction.integer(factors.get(j));
            }
            n++;
        }

        Fraction[][] constants = Fraction.new2DArray(extraRows + elements.size(), 1);
        constants[0][0] = Fraction.integer(1);

        List<Fraction> coefficients = Solver.solve(mat, constants);
        //System.out.println(coefficients);

        int lowestCommonDenominator = Fraction.lowestCommonDenominator(coefficients);

        Iterator<Fraction> it1 = coefficients.iterator();
        Iterator<Entry<Compound, Integer>> it2 = reactants.getCompounds().entrySet().iterator();
        Iterator<Entry<Compound, Integer>> it3 = products.getCompounds().entrySet().iterator();

        while (it1.hasNext()) {
            //        	System.out.print(a);
            int coefficient = it1.next().multiplySelfBy(lowestCommonDenominator).getNumerator();
            //        	System.out.println(coefficient);
            if (it2.hasNext()) {
                it2.next().setValue(coefficient);
            } else if (it3.hasNext()) {
                it3.next().setValue(coefficient);
            }
        }
        return new Reaction(reactants, products); //, false);
    }

    private static List<Integer> negative(List<Integer> list) {
        List<Integer> negative = new ArrayList<>();
        for (int i : list) {
            negative.add(-i);
        }
        return negative;
    }

    public Expression getReactants() {
        return reactants;
    }

    public Expression getProducts() {
        return products;
    }

    public static String getCompoundsString(Map<Compound, Integer> compounds, boolean grams) {
        //TODO use a StringBuilder for Reaction.getCompoundsString()
        String s = "";
        boolean first = true;
        for (Entry<Compound, Integer> entry : compounds.entrySet()) {
            Compound compound = entry.getKey();
            int quantity = entry.getValue();
            if (quantity != 0) {
                if (first) {
                    first = false;
                } else {
                    s += " + ";
                }
                if (quantity != 1 || grams) {
                    if (grams) {
                        s += quantity + "g ";
                    } else {
                        s += quantity + " ";
                    }
                }
                s += compound;
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return reactants + " => " + products;
    }


    public static void printMolarMasses(Map<Compound, Integer> compounds) {
        for (Entry<Compound, Integer> entry : compounds.entrySet()) {
            Compound c = entry.getKey();
            System.out.print(Fraction.round8(c.getMolarMass()) + "     ");
        }
    }

    public static void printDoubles(Map<Compound, Double> compounds) {
        for (Entry<Compound, Double> entry : compounds.entrySet()) {
            System.out.print(entry.getValue() + "         ");
        }
    }

    public void printInfo() {
        System.out.println(new ReactionStringTableBuilder().add().buildCompounds(reactants).add(" => ").buildCompounds(products).newRow().add("g/mol").buildMolarMasses(reactants).add().buildMolarMasses(products).newRow().add("mol").buildMoles(reactants).add().buildMoles(products).newRow().add("g").buildGrams(reactants).add().buildGrams(products).build(2, Align.CENTER));
    }

    public int getCoefficient(Compound c) {
        Integer coefficient = reactants.getCoefficient(c);
        if (coefficient == null) {
            coefficient = products.getCoefficient(c);
        }
        if (coefficient == null) {
            throw new InvalidKeyException("Compound \"" + c + "\" not contained in the reaction.");
        }
        return coefficient;
    }

    public Reaction setMoles(String s, double moles) {
        return setMoles(Compound.fromString(s), moles);
    }

    public Reaction setGrams(String s, double grams) {
        return setGrams(Compound.fromString(s), grams);
    }

    public Compound getAtIndex(int index) {
        if (index < reactants.size()) {
            return reactants.getAtIndex(index);
        } else if (index - reactants.size() < products.size()) {
            return products.getAtIndex(index - reactants.size());
        } else {
            return null;
        }
    }

    public Reaction setMoles(int i, int moles) {
        Compound compound = getAtIndex(i);
        if (compound == null) {
            System.out.println("Index out of range.");
            return this;
        } else {
            return setMoles(compound, moles);
        }
    }

    public Reaction setGrams(int i, double grams) {
        Compound compound = getAtIndex(i);
        if (compound == null) {
            System.out.println("Index out of range.");
            return this;
        } else {
            return setGrams(compound, grams);
        }
    }

    public Reaction setMoles(Compound compound, double moles) {
        double molesPerCoefficient;
        try {
            molesPerCoefficient = moles / getCoefficient(compound);
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
            return this;
        }
        reactants.setMolesPerCoefficient(molesPerCoefficient);
        products.setMolesPerCoefficient(molesPerCoefficient);
        return this;
    }

    public Reaction setGrams(Compound compound, double grams) {
        return setMoles(compound, grams / compound.getMolarMass());
    }
}
