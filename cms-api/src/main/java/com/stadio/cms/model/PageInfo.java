package com.stadio.cms.model;

import lombok.Data;

@Data
public class PageInfo
{
    private Integer current;
    private Integer total;
    private Long count;
    private Integer pageSize;

    private String actionNext;
    private String actionPrevious;

    public PageInfo(Integer current, Long count, Integer pageSize, String uri)
    {
        this.current = current;
        this.count = count;
        this.pageSize = pageSize;

        Double total = Math.ceil(new Double(count) / pageSize);
        setTotal(total.intValue());

        setActionNext(uri + "?page=" + (current + 1) + "&pageSize=" + pageSize);
        setActionPrevious(uri + "?page=" + (current - 1) + "&pageSize=" + pageSize);

        if (current <= 1)
        {
            setActionPrevious(null);
        }

        if (current >= total)
        {
            setActionNext(null);
        }
    }

}
