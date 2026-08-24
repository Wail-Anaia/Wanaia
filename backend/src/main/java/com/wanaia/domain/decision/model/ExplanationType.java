package com.wanaia.domain.decision.model;

public enum ExplanationType {
    PRO,      // Why WANAIA recommends this
    CON,      // Why you might not want this (trade-offs)
    WARNING   // Constraint alert (e.g. no home charging for EV)
}
