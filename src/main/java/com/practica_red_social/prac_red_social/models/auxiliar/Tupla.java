package com.practica_red_social.prac_red_social.models.auxiliar;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Tupla<A,B> {
    private A objeto1;
    private B objeto2;

    public Tupla(A objeto1, B objeto2){
        this.objeto1 = objeto1;
        this.objeto2 = objeto2;
    }
}
