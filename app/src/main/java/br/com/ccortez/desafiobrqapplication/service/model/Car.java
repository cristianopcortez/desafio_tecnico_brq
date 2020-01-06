package br.com.ccortez.desafiobrqapplication.service.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Car {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "nome")
    public String nome;

    @ColumnInfo(name = "marca")
    public String marca;

    @ColumnInfo(name = "descricao")
    public String descricao;

    @ColumnInfo(name = "quantidade")
    public int quantidade;

    @ColumnInfo(name = "preco")
    public int preco;

    @ColumnInfo(name = "imagem")
    public String imagem;

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                ", descricao='" + descricao + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", imagem='" + imagem + '\'' +
                '}';
    }
}
