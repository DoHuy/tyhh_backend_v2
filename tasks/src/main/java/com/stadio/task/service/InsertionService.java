package com.stadio.task.service;

import com.stadio.common.enu.FolderName;
import com.stadio.task.AppMainForTasks;
import com.stadio.task.baker.exam.ExamGenerator;
import com.stadio.task.baker.question.QuestionExtracter;
import com.stadio.task.baker.users.System2;
import com.stadio.task.baker.users.UserNameGenerator;
import com.stadio.common.service.PasswordService;
import com.stadio.common.utils.RandomUtils;
import com.stadio.model.documents.*;
import com.stadio.model.enu.QuestionLevel;
import com.stadio.model.enu.QuestionType;
import com.stadio.model.model.Answer;
import com.hoc68.users.defines.RoleType;
import com.hoc68.users.documents.User;
import com.stadio.model.repository.main.*;
import com.stadio.model.repository.user.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Andy on 01/20/2018.
 */
@Service
public class InsertionService extends AppMainForTasks
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired QuestionInExamRepository questionInExamRepository;

    @Autowired
    ClazzRepository clazzRepository;

    public void processGeneratingNames()
    throws Exception
    {
        Set<Object> used = new HashSet<>(userRepository.findAll());

        Set<String> f = UserNameGenerator.loadVietNameFamily();
        Set<String> m = UserNameGenerator.loadVietNameMaleMiddle();
        Set<String> g = UserNameGenerator.loadVietNameMaleGiven();
        Set<String> m1 = UserNameGenerator.loadVietNameFemaleMiddle();
        Set<String> g1 = UserNameGenerator.loadVietNameFemaleGiven();


        IntStream.range(0, 2000000).parallel().forEach(k ->
        {
            List<String> lk = (Math.random() > 0.5 ?
                    UserNameGenerator.nextName(f, m, g) :
                    UserNameGenerator.nextName(f, m1, g1) );

            String ek = UserNameGenerator.emailFromName(lk);
            if(!used.contains(ek))
            {
                System.out.println(lk + "; " + ek);
                String fullName = StringUtils.join(lk, " ");

                User user = new User();
                user.setUsername(ek);
                user.setEmail(ek);
                List<Clazz> clazzList = clazzRepository.findAll();

                if (clazzList != null && clazzList.size() != 0){
                    user.setClazzId(clazzList.get(RandomUtils.number(0, clazzList.size() - 1)).getId());
                }
                user.setPasswordRand(com.stadio.common.utils.StringUtils.identifier256());
                user.setPasswordHash(PasswordService.hidePassword("123456", user.getPasswordRand()));
                user.setFullName(fullName);
                try
                {
                    user.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse("29/02/1995"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                user.setAvatar("https://78.media.tumblr.com/f7f956f1efb231dff3672c7e3049e2ad/tumblr_inline_n3me8ePlS21sdnios.png");
                user.setCode(RandomStringUtils.randomNumeric(12));
                user.setUserRole(RoleType.User.toInt());
                userRepository.save(user);

                used.add(ek);
            }

        });

        System2.theEnd();
    }

    public void processInsert2QuestionBank()
    throws Exception
    {
        File stock = useFM().getDesktopFile("data-deploy");

        List<Clazz> clazzes = clazzRepository.findAll();

        for (File f: stock.listFiles())
        {
            System.out.println(">>>>>> Reading File: " + f.getName());
            List<String> questionList = QuestionExtracter.question(f);
            for (String sk: questionList)
            {
                sk = sk.replaceAll(System.lineSeparator(), "\t");

                Question question = new Question();
                String content = QuestionExtracter.parseQuestion(sk);
                if (content == null) continue;
                if (content.startsWith("Chọn")) continue;

                question.setContent(content.trim());

                Answer answer = new Answer();
                answer.setCode("A");
                String a = QuestionExtracter.parseAnswerA(sk).trim();
                if (a.length() == 0) continue;
                answer.setContent(a);
                answer.setCorrect(false);
                question.getAnswers().add(answer);

                answer = new Answer();
                answer.setCode("B");
                String b = QuestionExtracter.parseAnswerB(sk).trim();
                if (b.length() == 0) continue;
                answer.setContent(b);
                answer.setCorrect(false);
                question.getAnswers().add(answer);

                answer = new Answer();
                answer.setCode("C");
                String c = QuestionExtracter.parseAnswerC(sk).trim();
                if (c.length() == 0) continue;
                answer.setContent(c);
                answer.setCorrect(false);
                question.getAnswers().add(answer);

                answer = new Answer();
                answer.setCode("D");
                String d = QuestionExtracter.parseAnswerD(sk).trim();
                if (d.length() == 0) continue;
                answer.setContent(d);
                answer.setCorrect(false);
                question.getAnswers().add(answer);

                int rk = new Random().nextInt(4);
                for (int i = 0; i < question.getAnswers().size(); i++)
                {
                    if (i == rk)
                    {
                        question.getAnswers().get(i).setCorrect(true);
                        break;
                    }
                }



                question.setLevel(QuestionLevel.random());
                question.setType(QuestionType.random());

                int indexClazz = new Random().nextInt(clazzes.size());
                question.setClazzId(clazzes.get(indexClazz).getId());

                List<String> chapterList = clazzes.get(indexClazz).getIdChapters();
                int ck = new Random().nextInt(chapterList.size());
                question.setChapterId(chapterList.get(ck));

                questionRepository.save(question);
                System.out.println(question.toString());

            }
        }


    }

    public void processInsert2Exam() throws Exception{
        long questionQuantity = questionRepository.count();
        int maxvalue = 4000;
        while (maxvalue>questionQuantity)
            maxvalue-=100;

        List<Clazz> clazzes = clazzRepository.findAll();

        for(int i=0;i<10000;i++){

            int indexClazz = new Random().nextInt(clazzes.size());

            Exam exam = ExamGenerator.generateExam(i,clazzes.get(indexClazz).getId());
            final Exam examSave = examRepository.save(exam);
            CompletableFuture[] completedFutures = new CompletableFuture[50];
            for(int pos = 0 ;pos<50;pos++){
                final int position = pos;
                final int maxvalueFinal = maxvalue;
                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
                    int ranNumber = new RandomDataGenerator().nextInt(0,maxvalueFinal);
                    Question question = questionRepository.randomQuestion(ranNumber);
                    QuestionInExam questionInExam = new QuestionInExam();
                    questionInExam.setExam(examSave);
                    questionInExam.setQuestion(question);
                    questionInExam.setPosition(position);
                    questionInExamRepository.save(questionInExam);
                });
                completedFutures[pos] = completableFuture;
            }
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(completedFutures);
            combinedFuture.get();
        }
    }

    public void processInsert2UserExamSubmit() throws IOException {
        final Path rootLocation = Paths.get("/opt/hoc68-storage/USERS/submit/");
        Stream<Path> stream = Files.walk(rootLocation);
        stream.filter(Files::isDirectory).forEach(path -> {
            try{
                Path directory= path.getFileName();
                String directoryName = directory.toString();
                if(directoryName.equals("submit")) return;
                Path p = Paths.get("/opt/hoc68-storage/chemistry/USERS")
                        .resolve("save_by_feature")
                        .resolve("submit_exam")
                        .resolve(directoryName.substring(directoryName.length()-2))
                        .resolve(directoryName.substring(directoryName.length()-4,directoryName.length()-2))
                        .resolve(directoryName.substring(directoryName.length()-6,directoryName.length()-4))
                        .resolve(directoryName);
                if (!p.toFile().exists())
                {
                    Files.createDirectories(p);
                }

                coppyFolder(path,p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void coppyFolder(Path src,Path dest) throws IOException {
        Stream<Path> stream = Files.walk(src);
        stream.filter(Files::isRegularFile).forEach(path -> {
            try {
                Path destFile = dest.resolve(path.getFileName().toString());
                if (!destFile.toFile().exists())
                {
                    Files.createDirectories(destFile);
                }
                Files.copy(path,destFile,StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}