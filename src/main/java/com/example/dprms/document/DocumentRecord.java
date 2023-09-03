package com.example.dprms.document;

import java.util.Date;

public record DocumentRecord (Long id, Date uploadedAt, String documentName){}
