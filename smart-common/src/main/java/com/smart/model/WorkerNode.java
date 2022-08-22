package com.smart.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String hostName;

    private String port;

    private Integer type;

    private Date launchDate;

    private Date modified;

    private Date created;
}
