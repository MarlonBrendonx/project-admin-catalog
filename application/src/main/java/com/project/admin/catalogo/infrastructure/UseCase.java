package com.project.admin.catalogo.infrastructure;

public abstract class UseCase<IN, OUT> {
    //Por padrão os casos de uso na literatura implementam o pattern command
    //O que a classe faz é um comando e tem um unico metodo publico execute
    public abstract OUT execute(IN anIn);
}