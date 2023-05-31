package com.receitas.app.model;

/*
 * class Receita
 * id: 6 chars
 * titulo: texto
 * descricao: texto
 * autor: id
 * 
 * tempo de preparo: duracao em minutos
 * metodo de cozimento : texto
 * categoria: texto
 * ingredientes[ingrediente:texto]
 * instrucoes[ <passo:num, instrucao:texto> ]
 * avaliacoes[ <id_usuario:id, nota: int entre [0, 5] > ]
 * acesso recentes: fila com 7 posições possiveis; todo dia um numero (int) sai da fila (acessos do dia mais antigo) e os acessos do dia mais recente (hoje) entram
 * 
 * obterAvaliacaoMedia() // gera uma avaliacao baseado no atributo avaliacoes (ou entao procure um metodo mais efetivo de fazer isso)
 * adicionarAvaliacao(id_usuario, nota) // adiciona a avaliacao de um usuário
 * removerAvaliacao(id_usuario) // remove a avaliacao de um usuário 
*/

public class Recipe {

  String id;
  String titulo;
  String descricao;

  public Recipe(String id, String titulo, String descricao){
	this.id = id;
	this.titulo = titulo;
	this.descricao = descricao;
  }

}
