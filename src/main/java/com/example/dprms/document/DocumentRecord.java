package com.example.dprms.document;

import java.util.Date;

public record DocumentRecord (Long id,String documentTitle, String documentName, String filePath, Date uploadedAt){}
