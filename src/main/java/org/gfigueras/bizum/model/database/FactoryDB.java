package org.gfigueras.bizum.model.database;

public class FactoryDB {
    public final static Integer MODO_MYSQL = 0;
    public final static Integer MODO_TEST = 0;
    
    public static IDAODB getDB(Integer modo) {
        if (modo == MODO_MYSQL) {
            return new DAODB();
        } else if (modo == MODO_TEST) {
            return null;
        } else {
            return null;
        }
    }
}
