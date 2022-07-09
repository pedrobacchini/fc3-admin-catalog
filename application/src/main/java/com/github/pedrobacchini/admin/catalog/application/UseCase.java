package com.github.pedrobacchini.admin.catalog.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIN);
}