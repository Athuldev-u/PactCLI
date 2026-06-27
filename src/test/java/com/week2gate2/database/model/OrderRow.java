package com.week2gate2.database.model;

import java.math.BigDecimal;
import java.time.Instant;
public record OrderRow(long id, String OrderNumber, String status, BigDecimal getTotal, String getUserId, Instant getCreatedAt)
{

}


