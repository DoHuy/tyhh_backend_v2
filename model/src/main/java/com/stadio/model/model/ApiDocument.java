package com.stadio.model.model;

import com.stadio.common.enu.Target;
import com.stadio.common.utils.StringUtils;
import lombok.Data;

/**
 * Created by Andy on 11/10/2017.
 */
@Data
public class ApiDocument
{
    private String name;
    private String path;
    private String parent;
    private String child;
    private String method;
    private String headers;
    private String description;
    private String request;
    private String response;
    private Target tar;

    public void setChild(String child)
    {
        this.child = child;
        if (StringUtils.isNotNull(this.parent))
        {
            if (this.parent.endsWith("/") || this.child.startsWith("/"))
                this.path = parent + child;
            else
                this.path = parent + "/" + child;
        } else {
            this.parent = "/" + child;
        }
    }

}
