package analizador;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * Proyecto Analizador
 *
 */
public class GraphAnalizador extends javax.swing.JFrame {

    String linea;
    String[] lineavec = new String[100];
    String[] lineavec2 = new String[100];
    String[] tokens = new String[100];
    String[] tokens2 = new String[100];
    String[][] tablaerrores = new String[100][2];
    String[][] tablasimbolos = new String[100][5];
    String[] lineastripletas = new String[100];
    String[][] tablaopsides = new String[8][2];
    String[][] tablaopsrelides = new String[8][2];
    String[][] tablatripletas = new String[100][4];
    int conterrores = 0;
    int contsimbolos = 0;
    int contlineas = 1;
    int conttokens = 1;
    int conttokens2 = 1;
    int contlineatripletas = 0;
    int contide = 1;
    int conttripletas = 0;
    int conttrip = 0;
    int contT = 1;
    int contC = 1;
    int contTR = 1;
    int apuntadorfor = 0;
    int apuntadorfor2 = 0;
    int apuntadorvueltafor = 0;
    int apuntadorvueltafor2 = 0;
    boolean banderasimbol = false;
    boolean banderavar = false;
    boolean banderaops = false;
    boolean banderaconst = false;
    boolean banderaerror = false;
    boolean banderafor = false;
    boolean banderafor2 = false;
    //--Palabras Clave permitidas y funciones aritmeticas--//
    String tipodatos[] = {"int", "boolean", "double", "String"};
    String operadores[] = {"+", "-", "*", "/", "%", "="};
    //--Declaracion de las ventanas--//
    DefaultTableModel modelo, modelo2, modelo3;
    int numint;
    boolean valbool;
    double numdouble;
    String valStr;
    String tipo = "";
    //--Ruta del archivo--//
    String fichero = "Codigo.Txt";

    public GraphAnalizador() {
        initComponents();
        //--Tablas de la interfaz declaradas--//
        modelo = (DefaultTableModel) jTable1.getModel();
        modelo2 = (DefaultTableModel) jTable2.getModel();
        modelo3 = (DefaultTableModel) jTable3.getModel();

        //--Metodo para leer archivo de texto--//
        try {
            FileInputStream fis = new FileInputStream(fichero);
            InputStreamReader isr = new InputStreamReader(fis, "utf8");
            BufferedReader br = new BufferedReader(isr);
            String lineadetexto;
            BufferedReader lectura;
            try {
                lectura = new BufferedReader(new FileReader(fichero));
                String codigo;
                while (lectura.ready()) {
                    codigo = lectura.readLine();
                    jTextArea1.setText(jTextArea1.getText() + contlineas + ":      " + codigo + "\n");
                    contlineas++;
                }
            } catch (IOException e) {
            }
            contlineas = 0;

            //--Pasa por cada una de las lineas--//
            while ((lineadetexto = br.readLine()) != null) {
                linea = lineadetexto;
                contlineas++;

                //--Separa todos los caracteres de la linea--//
                for (int i = 0; i < linea.length(); i++) {
                    lineavec[i] = "" + linea.charAt(i);
                }

                //--Cuenta los espacios para saber el numero de tokens--//
                for (int i = 0; i < linea.length(); i++) {
                    if (" ".equals(lineavec[i])) {
                        conttokens++;
                    }
                }
                //--Asigna y libera un espacio a los tokens--//
                for (int i = 0; i < conttokens; i++) {
                    tokens[i] = "";
                }
                conttokens = 0;

                //--Agrega los tokens al arreglo--//
                for (int i = 0; i < linea.length(); i++) {
                    if (!" ".equals(lineavec[i])) {
                        if (";".equals(lineavec[i + 1])) {
                            tokens[conttokens] = "" + tokens[conttokens] + lineavec[i];
                            conttokens++;
                            tokens[conttokens] = "" + lineavec[i + 1];
                            i++;
                        } else {
                            tokens[conttokens] = "" + tokens[conttokens] + lineavec[i];
                        }
                    } else {
                        conttokens++;
                    }
                }

                //--Recorre cada token--//
                for (int i = 0; (i < conttokens + 1 && !";".equals(tokens[i])); i++) {
                    banderavar = false;
                    banderaops = false;
                    banderaconst = false;

                    //--Revisa si se define una variable con un tipo de dato (int, boolean, double...)--//
                    for (int x = 0; (x < tipodatos.length && banderasimbol == false); x++) {
                        if (tokens[i].equals(tipodatos[x])) {
                            tablasimbolos[contsimbolos][0] = "" + contlineas;
                            tablasimbolos[contsimbolos][1] = tokens[0];
                            tablasimbolos[contsimbolos][2] = tokens[1];
                            if (!";".equals(tokens[2])) {
                                tablasimbolos[contsimbolos][3] = tokens[3];
                            }
                            tablasimbolos[contsimbolos][4] = "IDE0" + contide;
                            contide++;
                            contsimbolos++;
                            banderasimbol = true;
                        }

                        //--Detecta que los tipos de datos sean compatibles--//
                        //--Deteccion de enteros--//
                        if (!";".equals(tokens[2])) {
                            if ("int".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3])) && banderaerror == false) {

                                try {
                                    numint = Integer.parseInt(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaerrores[conterrores][0] = "" + contlineas;
                                    tablaerrores[conterrores][1] = "Tipo de variable (int) incompatible";
                                    conterrores++;
                                    banderaerror = true;
                                }

                            } else //--Deteccion de booleanos--// 
                            if ("boolean".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3])) && banderaerror == false) {
                                if (!"true".equals(tokens[3]) && !"false".equals(tokens[3])) {
                                    tablaerrores[conterrores][0] = "" + contlineas;
                                    tablaerrores[conterrores][1] = "Tipo de variable (boolean) incompatible";
                                    conterrores++;
                                    banderaerror = true;
                                }
                            } else //--Deteccion de decimales--//
                            if ("double".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3])) && banderaerror == false) {
                                try {
                                    numdouble = Double.parseDouble(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaerrores[conterrores][0] = "" + contlineas;
                                    tablaerrores[conterrores][1] = "Tipo de variable (double) incompatible";
                                    conterrores++;
                                    banderaerror = true;
                                }
                            } else //--Deteccion de Strings--//
                            if ("String".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3])) && banderaerror == false) {
                                if (tokens[3].charAt(0) != '"' || tokens[3].charAt(tokens[3].length() - 1) != '"') {
                                    tablaerrores[conterrores][0] = "" + contlineas;
                                    tablaerrores[conterrores][1] = "Tipo de variable (String) incompatible o mal definida";
                                    conterrores++;
                                    banderaerror = true;
                                }
                            }
                        }
                    }
                    //Revision y correcion del ciclo FOR--//
//                     if ("FOR".equals(tokens[0])) {
//                        banderafor2 = true;
//                        if (tokens[1].charAt(0) != '(') {
//                            tablaerrores[conterrores][0] = "" + contlineas;
//                            tablaerrores[conterrores][1] = "No se ha abierto el parentesis del FOR";
//                            conterrores++;
//                        } else {
//                            if (!";".equals(tokens[6]) || !";".equals(tokens[10])) {
//                                tablaerrores[conterrores][0] = "" + contlineas;
//                                tablaerrores[conterrores][1] = "Falta el ';' del separador en FOR";
//                                conterrores++;
//                                banderaerror = true;
//                            }
//                        }
//                        if (tokens[conttokens].charAt(tokens[conttokens].length()-1) != ')') {
//                            tablaerrores[conterrores][0] = "" + contlineas;
//                            tablaerrores[conterrores][1] = "No se ha cerrado el parentesis del FOR";
//                            conterrores++;
//                        }
//                        i = conttokens;
//                    }
//                    if ("ENDFOR".equals(tokens[0])) {
//                        if (banderafor2 == false) {
//                            tablaerrores[conterrores][0] = "" + contlineas;
//                            tablaerrores[conterrores][1] = "No se ha abierto in ciclo FOR";
//                            conterrores++;
//                        } else banderafor2 = false;
//                    }
//                    
//                    //--Revisa si existe alguna variable--//
//                    for (int y = 0; (y < tablasimbolos.length && banderasimbol == false); y++) {
//                        if (tokens[i].equals(tablasimbolos[y][2])) banderavar = true;
//                    }
//                    
//                    //--Ignora operadores--//
//                    for (int y = 0; (y < operadores.length && banderasimbol == false); y++) {
//                        if (tokens[i].equals(operadores[y])) banderaops = true;
//                    }
//                    
//                    //--Ignora constantes--//
//                    try{
//                        numint = Integer.parseInt(tokens[i]);
//                        banderaconst = true;
//                    }catch(NumberFormatException e){}
//                    try{
//                        numdouble = Double.parseDouble(tokens[i]);
//                        banderaconst = true;
//                    }catch(NumberFormatException e){}
//                    if (tokens[i].charAt(0) == '"' && tokens[i].charAt(tokens[i].length()-1) == '"') {
//                        try{
//                            valStr = String.valueOf((tokens[i]));
//                            banderaconst = true;
//                        }catch(NumberFormatException e){}
//                    }
//                    
//                     //--Checa los tokens excluidos--//
//                    if (banderasimbol == false  && !"FOR".equals(tokens[0]) && !"ENDFOR".equals(tokens[0])) {
//                        if (banderavar == false && banderaops == false && banderaconst == false) {
//                            tablaerrores[conterrores][0] = "" + contlineas;
//                            tablaerrores[conterrores][1] = "Variable " + tokens[i] + " no declarada";
//                            conterrores++;
//                            banderaerror = true;
//                        }
//                    }
//                    
//                    //--Deteccion de tipos incompatibles--//
//                    for (int y = 0; (y < tablasimbolos.length && banderasimbol == false); y++) {
//                        if (tokens[i].equals(tablasimbolos[y][2])) {
//                            if (!"".equals(tipo) && !tablasimbolos[y][1].equals(tipo)) {
//                                tablaerrores[conterrores][0] = "" + contlineas;
//                                tablaerrores[conterrores][1] = "Tipo de variables incompatible";
//                                conterrores++;
//                                banderaerror = true;
//                            }
//                            tipo = tablasimbolos[y][1];
//                        }
//                    }
//                }
//                
//                //--Revisa error si termina con ; dentro del ciclo FOR--//
//                if (!";".equals(tokens[conttokens]) && !"FOR".equals(tokens[0]) && !"ENDFOR".equals(tokens[0])) {
//                    tablaerrores[conterrores][0] = "" + contlineas;
//                    tablaerrores[conterrores][1] = "Hace falta ;";
//                    conterrores++;
//                }
//                //--Si se cumple con los requerimientos--//
//                if (banderaerror == false) {
//                    lineastripletas[contlineatripletas] = linea;
//                    contlineatripletas++;
//                }
//                
//                banderasimbol = false;
//                banderavar = false;
//                banderaops = false;
//                banderaconst = false;
//                banderaerror = false;
//                tipo = "";
//                }
//            
//                if (banderafor2 == true) {
//                    tablaerrores[conterrores][0] = "" + contlineas;
//                    tablaerrores[conterrores][1] = "El ciclo FOR no tiene un ENDFOR;";
//                    conterrores++;
//                }
//                    fis.close();
//                } catch(IOException e) {}
//                
//                tablaopsides[0][0] = "+"; tablaopsides[0][1] = "OPA01";
//                tablaopsides[1][0] = "-"; tablaopsides[1][1] = "OPA02";
//                tablaopsides[2][0] = "*"; tablaopsides[2][1] = "OPA03";
//                tablaopsides[3][0] = "/"; tablaopsides[3][1] = "OPA04";
//                tablaopsides[4][0] = "%"; tablaopsides[4][1] = "OPA05";
//
//                tablaopsrelides[0][0] = "="; tablaopsrelides[0][1] = "OPR01";
//                tablaopsrelides[1][0] = "<"; tablaopsrelides[1][1] = "OPR02";
//                tablaopsrelides[2][0] = ">"; tablaopsrelides[2][1] = "OPR03";
//                tablaopsrelides[3][0] = "<="; tablaopsrelides[3][1] = "OPR04";
//                tablaopsrelides[4][0] = ">="; tablaopsrelides[4][1] = "OPR05";

                    //--Revision y correccion del ciclo WHILE--//
                    if ("WHILE".equals(tokens[0])) {
                        banderafor = true;
                        if (tokens[1].charAt(0) != '(') {
                            tablaerrores[conterrores][0] = "" + contlineas;
                            tablaerrores[conterrores][1] = "No se ha abierto el parentesis del WHILE";
                            conterrores++;
                        } //else {
//                           if (!";".equals(tokens[6]) || !";".equals(tokens[10])) {
//                                tablaerrores[conterrores][0] = "" + contlineas;
//                                tablaerrores[conterrores][1] = "Falta el ';' del separador en WHILE";
//                                conterrores++;
//                                banderaerror = true;
//                            }
                        //}
                        if (tokens[conttokens].charAt(tokens[conttokens].length() - 1) != ')') {
                            tablaerrores[conterrores][0] = "" + contlineas;
                            tablaerrores[conterrores][1] = "No se ha cerrado el parentesis del WHILE";
                            conterrores++;
                        }
                        i = conttokens;
                    }
                    if ("ENDWHILE".equals(tokens[0])) {
                        if (banderafor == false) {
                            tablaerrores[conterrores][0] = "" + contlineas;
                            tablaerrores[conterrores][1] = "No se ha abierto un ciclo WHILE";
                            conterrores++;
                        } else {
                            banderafor = false;
                        }
                    }

                    //--Revisa si existe alguna variable--//
                    for (int x = 0; (x < tablasimbolos.length && banderasimbol == false); x++) {
                        if (tokens[i].equals(tablasimbolos[x][2])) {
                            banderavar = true;
                        }
                    }

                    //--Ignora operadores--//
                    for (int x = 0; (x < operadores.length && banderasimbol == false); x++) {
                        if (tokens[i].equals(operadores[x])) {
                            banderaops = true;
                        }
                    }

                    //--Ignora constantes--//
                    try {
                        numint = Integer.parseInt(tokens[i]);
                        banderaconst = true;
                    } catch (NumberFormatException e) {
                    }
                    try {
                        numdouble = Double.parseDouble(tokens[i]);
                        banderaconst = true;
                    } catch (NumberFormatException e) {
                    }
                    if (tokens[i].charAt(0) == '"' && tokens[i].charAt(tokens[i].length() - 1) == '"') {
                        try {
                            valStr = String.valueOf((tokens[i]));
                            banderaconst = true;
                        } catch (NumberFormatException e) {
                        }
                    }

                    //--Checa los tokens excluidos--//
                    if (banderasimbol == false && !"WHILE".equals(tokens[0]) && !"ENDWHILE".equals(tokens[0])) {
                        if (banderavar == false && banderaops == false && banderaconst == false) {
                            tablaerrores[conterrores][0] = "" + contlineas;
                            tablaerrores[conterrores][1] = "Sentencia " + tokens[i] + " no encontrada";
                            conterrores++; //
                            banderaerror = true; //
                        }
                    }

                    //--Deteccion de tipos incompatibles--//
                    for (int x = 0; (x < tablasimbolos.length && banderasimbol == false); x++) {
                        if (tokens[i].equals(tablasimbolos[x][2])) {
                            if (!"".equals(tipo) && !tablasimbolos[x][1].equals(tipo)) {
                                tablaerrores[conterrores][0] = "" + contlineas;
                                tablaerrores[conterrores][1] = "Tipo de variables incompatible";
                                conterrores++;
                                banderaerror = true;
                            }
                            tipo = tablasimbolos[x][1];
                        }
                    }
                }

                //--Revisa error si termina con ; dentro del ciclo WHILE--//
                if (!";".equals(tokens[conttokens]) && !"WHILE".equals(tokens[0]) && !"ENDWHILE".equals(tokens[0])) {
                    tablaerrores[conterrores][0] = "" + contlineas;
                    tablaerrores[conterrores][1] = "Hace falta ;";
                    conterrores++;
                }
                //--Si se cumple con los requerimientos--//
                if (banderaerror == false) {
                    lineastripletas[contlineatripletas] = linea;
                    contlineatripletas++;
                }

                banderasimbol = false;
                banderavar = false;
                banderaops = false;
                banderaconst = false;
                banderaerror = false;
                tipo = "";
            }
            if (banderafor == true) {
                tablaerrores[conterrores][0] = "" + contlineas;
                tablaerrores[conterrores][1] = "El ciclo WHILE no tiene un ENDWHILE;";
                conterrores++;
            }
            fis.close();
        } catch (IOException e) {
        }

        tablaopsides[0][0] = "+";
        tablaopsides[0][1] = "OPA01";
        tablaopsides[1][0] = "-";
        tablaopsides[1][1] = "OPA02";
        tablaopsides[2][0] = "*";
        tablaopsides[2][1] = "OPA03";
        tablaopsides[3][0] = "/";
        tablaopsides[3][1] = "OPA04";
        tablaopsides[4][0] = "%";
        tablaopsides[4][1] = "OPA05";

        tablaopsrelides[0][0] = "=";
        tablaopsrelides[0][1] = "OPR01";
        tablaopsrelides[1][0] = "<";
        tablaopsrelides[1][1] = "OPR02";
        tablaopsrelides[2][0] = ">";
        tablaopsrelides[2][1] = "OPR03";
        tablaopsrelides[3][0] = "<=";
        tablaopsrelides[3][1] = "OPR04";
        tablaopsrelides[4][0] = ">=";
        tablaopsrelides[4][1] = "OPR05";
        tablaopsrelides[5][0] = "==";
        tablaopsrelides[5][1] = "OPR06";
        tablaopsrelides[6][0] = "!=";
        tablaopsrelides[6][1] = "OPR07";
        tablaopsrelides[7][0] = "||";
        tablaopsrelides[7][1] = "OPR08";

        // --Insertar valores en tabla simbolos ciclo WHILE --//
        for (int x = 0; x < contlineatripletas; x++) {
            System.out.println(lineastripletas[x]);
            //--Separa todos los caracteres de la linea--//
            for (int i = 0; i < lineastripletas[x].length(); i++) {
                lineavec2[i] = "" + lineastripletas[x].charAt(i);
            }
            //--Cuenta los espacios para saber el numero de tokens--//
            for (int i = 0; i < lineastripletas[x].length(); i++) {
                if (" ".equals(lineavec2[i])) {
                    conttokens2++;
                }
            }
            //--Asigna y libera un espacio a los tokens--//
            for (int i = 0; i < conttokens2; i++) {
                tokens2[i] = "";
            }
            conttokens2 = 0;
            //--Agrega los tokens al arreglo--//
            for (int i = 0; i < lineastripletas[x].length(); i++) {
                if (!" ".equals(lineavec2[i])) {
                    if (";".equals(lineavec2[i + 1])) {
                        tokens2[conttokens2] = "" + tokens2[conttokens2] + lineavec2[i];
                        conttokens2++;
                        tokens2[conttokens2] = "" + lineavec2[i + 1];
                        i++;
                    } else {
                        tokens2[conttokens2] = "" + tokens2[conttokens2] + lineavec2[i];
                    }
                } else {
                    conttokens2++;
                }
            }
            //--Recorre cada token de las tripletas--//
            for (int i = 0; (i < conttokens2 + 1 && !";".equals(tokens2[i])); i++) {
//            for (int i = 0; (i < conttokens2+1 ); i++) {
                for (String tipodato : tipodatos) {
                    if (tokens2[0].equals(tipodato)) {
                        tablatripletas[conttrip][0] = "" + (conttrip);
                        tablatripletas[conttrip][1] = "T" + contT;
                        tablatripletas[conttrip][2] = "CE" + contC;
                        tablatripletas[conttrip][3] = "=";
                        tablatripletas[conttrip + 1][0] = "" + (conttrip + 1);
                        for (String[] tablasimbolo : tablasimbolos) {
                            if (tokens2[1].equals(tablasimbolo[2])) {
                                tablatripletas[conttrip + 1][1] = tablasimbolo[4];
                            }
                        }
                        tablatripletas[conttrip + 1][2] = "T" + contT;
                        tablatripletas[conttrip + 1][3] = "OPR01";
                        for (String[] tablaopsrelide : tablaopsrelides) {
                            if (tokens2[2].equals(tablaopsrelide[0])) {
                                tablatripletas[conttrip + 1][3] = tablaopsrelide[1];
                            }
                        }
                        contT++;
                        contC++;
                        conttrip++;
                        conttrip++;
                        i = conttokens2 + 1;
                    }
                }

                if (";".equals(tokens2[3])) {
                    tablatripletas[conttrip][0] = "" + (conttrip);
                    tablatripletas[conttrip][1] = "T" + contT;
                    for (String[] tablasimbolo : tablasimbolos) {
                        if (tokens2[2].equals(tablasimbolo[2])) {
                            tablatripletas[conttrip][2] = tablasimbolo[4];
                        }
                    }
                    if ("".equals(tablatripletas[conttrip][2]) || tablatripletas[conttrip][2] == null) {
//                        tablatripletas[conttrip][2] = "CE" + contC;
//                        contC++;
                    }
                    tablatripletas[conttrip][3] = "=";
                    tablatripletas[conttrip + 1][0] = "" + (conttrip + 1);
                    for (String[] tablasimbolo : tablasimbolos) {
                        if (tokens2[0].equals(tablasimbolo[2])) {
                            tablatripletas[conttrip + 1][1] = tablasimbolo[4];
                        }
                    }
                    tablatripletas[conttrip + 1][2] = tablatripletas[conttrip][1];
                    tablatripletas[conttrip + 1][3] = tablaopsrelides[0][1];
                    contT++;
                    conttrip++;
                    conttrip++;
                    i = conttokens2 + 1;
                }

                if (";".equals(tokens2[5])) {
                    tablatripletas[conttrip][0] = "" + (conttrip);
                    tablatripletas[conttrip][1] = "T" + contT;
                    for (String[] tablasimbolo : tablasimbolos) {
                        if (tokens2[2].equals(tablasimbolo[2])) {
                            tablatripletas[conttrip][2] = tablasimbolo[4];
                        }
                    }
                    if ("".equals(tablatripletas[conttrip][2]) || tablatripletas[conttrip][2] == null) {
                        tablatripletas[conttrip][2] = "CE" + contC;
                        contC++;
                    }
                    tablatripletas[conttrip][3] = "=";
                    if ((("+".equals(tokens2[3]) || "-".equals(tokens2[3])) && "0".equals(tokens2[4]))
                            || (("*".equals(tokens2[3]) || "/".equals(tokens2[3])) && "1".equals(tokens2[4]))) {
                    } else {
                        tablatripletas[conttrip + 1][0] = "" + (conttrip + 1);
                        tablatripletas[conttrip + 1][1] = tablatripletas[conttrip][1];
                        for (String[] tablasimbolo : tablasimbolos) {
                            if (tokens2[4].equals(tablasimbolo[2])) {
//                            tablatripletas[conttrip+1][2] = tablasimbolo[4];
                                tablatripletas[conttrip + 1][2] = tablasimbolo[4];
                            }
                        }
                        if ("".equals(tablatripletas[conttrip + 1][2]) || tablatripletas[conttrip + 1][2] == null) {
//                        tablatripletas[conttrip][2] = "CE" + contC;
                            tablatripletas[conttrip + 1][2] = "CE" + contC;
                            contC++;
                        }
                        for (String[] tablaopside : tablaopsides) {
                            if (tokens2[3].equals(tablaopside[0])) {
                                tablatripletas[conttrip + 1][3] = tablaopside[1];
                            }
                        }
                        tablatripletas[conttrip + 2][0] = "" + (conttrip + 2);
                        for (String[] tablasimbolo : tablasimbolos) {
                            if (tokens2[0].equals(tablasimbolo[2])) {
                                tablatripletas[conttrip + 2][1] = tablasimbolo[4];
                            }
                        }
                        tablatripletas[conttrip + 2][2] = tablatripletas[conttrip][1];
                        tablatripletas[conttrip + 2][3] = tablaopsrelides[0][1];
                        contT++;
                        conttrip++;
                        conttrip++;
                        conttrip++;
                        i = conttokens2 + 1;
                    }
                }
                if ("WHILE".equals(tokens2[0])) {
                    apuntadorvueltafor = conttrip;
                    for (String tipodato : tipodatos) {
                        if (tokens2[2].equals(tipodato)) {
//                            tablatripletas[conttrip][0] = "" + (conttrip);
//                            tablatripletas[conttrip][1] = "T" + contT;
//                            tablatripletas[conttrip][2] = "CE" + contC;
//                            tablatripletas[conttrip][3] = "=";
//                           tablatripletas[conttrip+1][0] = 
                            tablatripletas[conttrip][0] = "" + (conttrip);
                            tablatripletas[conttrip][1] = "IDETEMP";
                            tablatripletas[conttrip][2] = "CE" + contC;
//                            tablatripletas[conttrip][3] = "OPR01";     
                            for (String[] tablaopsrelide : tablaopsrelides) {
                                if (tokens2[4].equals(tablaopsrelide[0])) {
//                                    tablatripletas[conttrip + 1][3] = tablaopsrelide[1];
                                    tablatripletas[conttrip][3] = tablaopsrelide[1];
                                }
                            }
//                            contT++;
                            contC++;
                            conttrip++;
//                            conttrip++;
                            i = conttokens2 + 1;
                        }
                    }

//                    tablatripletas[conttrip][0] = "" + (conttrip);
//                    tablatripletas[conttrip][1] = "T" + contT;
//                    tablatripletas[conttrip][2] = "CE" + contC;
//                    tablatripletas[conttrip][3] = "=";
//                    contC++;
//                    contT++;
//                    conttrip++;
//                    tablatripletas[conttrip][0] = "" + (conttrip);
//                    tablatripletas[conttrip][1] = "T" + (contT);
//                    tablatripletas[conttrip][2] = "T" + (contT-1);
//                    for (String[] tablaopsrelide : tablaopsrelides) {
//                        if (tokens2[8].equals(tablaopsrelide[0])) {
//                            tablatripletas[conttrip][3] = tablaopsrelide[1];
//                        }
//                    }
//                    contT++;
//                    contC++;
//                    conttrip++;
                    tablatripletas[conttrip][0] = "" + (conttrip);
                    tablatripletas[conttrip][1] = "TR" + (contTR);
                    tablatripletas[conttrip][2] = "TRUE";
                    tablatripletas[conttrip][3] = "" + (conttrip + 2);
                    conttrip++;
                    tablatripletas[conttrip][0] = "" + (conttrip);
                    tablatripletas[conttrip][1] = "TR" + (contTR);
                    tablatripletas[conttrip][2] = "FALSE";
//                    apuntadorfor = 1 + (conttrip);
                    apuntadorfor = conttrip;
                    conttrip++;
                    contTR++;

                }
                if ("ENDWHILE".equals(tokens2[0]) && apuntadorfor > 0) {
//                    tablatripletas[conttrip][0] = "" + (conttrip);
//                    tablatripletas[conttrip][1] = tablatripletas[apuntadorfor - 5][1];
//                    tablatripletas[conttrip][2] = tablatripletas[apuntadorfor - 5][2];
//                    tablatripletas[conttrip][3] = "OPA01"; 
//                    conttrip++;
                    tablatripletas[conttrip][0] = "" + (conttrip);
                    tablatripletas[conttrip][1] = "JP";
//                    tablatripletas[conttrip][3] = "" + (apuntadorvueltafor + 1); 
                    tablatripletas[conttrip][3] = "" + (apuntadorvueltafor);
                    //apuntador asignador de ultimo lugar dentro del while
                    tablatripletas[apuntadorfor][3] = "" + (conttrip + 1);
                    conttrip++;
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGeneral = new javax.swing.JPanel();
        PanelSimbolos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelCodigo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        PanelErrores = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        PanelBoton = new javax.swing.JPanel();
        BtnDesplegar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        PanelTripletas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador");
        setBackground(new java.awt.Color(18, 150, 200));
        setForeground(new java.awt.Color(18, 150, 200));
        setUndecorated(true);
        setResizable(false);

        PanelGeneral.setBackground(new java.awt.Color(255, 51, 0));

        PanelSimbolos.setBackground(new java.awt.Color(255, 51, 0));
        PanelSimbolos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tabla Simbolos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable1.setBackground(new java.awt.Color(214, 212, 212));
        jTable1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Tipo de dato", "Variable", "Null", "IDE"
            }
        ));
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        javax.swing.GroupLayout PanelSimbolosLayout = new javax.swing.GroupLayout(PanelSimbolos);
        PanelSimbolos.setLayout(PanelSimbolosLayout);
        PanelSimbolosLayout.setHorizontalGroup(
            PanelSimbolosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSimbolosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        PanelSimbolosLayout.setVerticalGroup(
            PanelSimbolosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSimbolosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelCodigo.setBackground(new java.awt.Color(255, 51, 0));
        PanelCodigo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Seccion Código", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout PanelCodigoLayout = new javax.swing.GroupLayout(PanelCodigo);
        PanelCodigo.setLayout(PanelCodigoLayout);
        PanelCodigoLayout.setHorizontalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelCodigoLayout.setVerticalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        PanelErrores.setBackground(new java.awt.Color(255, 51, 0));
        PanelErrores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Errores Semánticos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable2.setBackground(new java.awt.Color(255, 51, 51));
        jTable2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Error"
            }
        ));
        jTable2.setEnabled(false);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(50);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout PanelErroresLayout = new javax.swing.GroupLayout(PanelErrores);
        PanelErrores.setLayout(PanelErroresLayout);
        PanelErroresLayout.setHorizontalGroup(
            PanelErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelErroresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelErroresLayout.setVerticalGroup(
            PanelErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelErroresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelBoton.setBackground(new java.awt.Color(255, 51, 0));
        PanelBoton.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Ejecución", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        BtnDesplegar.setFont(new java.awt.Font("NSimSun", 1, 18)); // NOI18N
        BtnDesplegar.setText("Compilar");
        BtnDesplegar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnDesplegar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnDesplegarMouseClicked(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("NSimSun", 1, 18)); // NOI18N
        jButton1.setText("Salir");
        jButton1.setMaximumSize(new java.awt.Dimension(123, 29));
        jButton1.setMinimumSize(new java.awt.Dimension(123, 29));
        jButton1.setPreferredSize(new java.awt.Dimension(123, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("NSimSun", 1, 18)); // NOI18N
        jButton2.setText("Limpiar");
        jButton2.setMaximumSize(new java.awt.Dimension(123, 29));
        jButton2.setMinimumSize(new java.awt.Dimension(123, 29));
        jButton2.setPreferredSize(new java.awt.Dimension(123, 29));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("NSimSun", 1, 18)); // NOI18N
        jButton3.setText("Abrir archivo");

        javax.swing.GroupLayout PanelBotonLayout = new javax.swing.GroupLayout(PanelBoton);
        PanelBoton.setLayout(PanelBotonLayout);
        PanelBotonLayout.setHorizontalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnDesplegar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanelBotonLayout.setVerticalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonLayout.createSequentialGroup()
                .addComponent(BtnDesplegar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton3))
        );

        PanelTripletas.setBackground(new java.awt.Color(255, 51, 0));
        PanelTripletas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tabla de Tripletas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable3.setBackground(new java.awt.Color(240, 240, 240));
        jTable3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Dato Objeto", "Dato Fuente", "Operador"
            }
        ));
        jTable3.setEnabled(false);
        jScrollPane4.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        javax.swing.GroupLayout PanelTripletasLayout = new javax.swing.GroupLayout(PanelTripletas);
        PanelTripletas.setLayout(PanelTripletasLayout);
        PanelTripletasLayout.setHorizontalGroup(
            PanelTripletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTripletasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelTripletasLayout.setVerticalGroup(
            PanelTripletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTripletasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout PanelGeneralLayout = new javax.swing.GroupLayout(PanelGeneral);
        PanelGeneral.setLayout(PanelGeneralLayout);
        PanelGeneralLayout.setHorizontalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelErrores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelTripletas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PanelBoton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(PanelSimbolos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelGeneralLayout.setVerticalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelSimbolos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelGeneralLayout.createSequentialGroup()
                                .addComponent(PanelTripletas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(PanelErrores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PanelBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 42, Short.MAX_VALUE))))
        );

        PanelBoton.getAccessibleContext().setAccessibleName("Seleccion de despliegue\n");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(PanelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnDesplegarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDesplegarMouseClicked
        int filas = modelo.getRowCount();
        for (int i = 1; i <= filas; i++) {
            modelo.removeRow(0);
        }
        for (int i = 0; i < contsimbolos; i++) {
            modelo.addRow(new Object[]{tablasimbolos[i][0], tablasimbolos[i][1], tablasimbolos[i][2],
                tablasimbolos[i][3], tablasimbolos[i][4]});
        }
        int filas2 = modelo2.getRowCount();
        for (int i = 1; i <= filas2; i++) {
            modelo2.removeRow(0);
        }
        for (int i = 0; i < conterrores; i++) {
            modelo2.addRow(new Object[]{tablaerrores[i][0], tablaerrores[i][1]});
        }
        int filas3 = modelo3.getRowCount();
        for (int i = 1; i <= filas3; i++) {
            modelo3.removeRow(0);
        }
        for (int i = 0; i < conttrip; i++) {
            modelo3.addRow(new Object[]{tablatripletas[i][0], tablatripletas[i][1], tablatripletas[i][2], tablatripletas[i][3]});
        }

        try {
            File archivo = new File("Triple.txt");
            FileWriter escribir = new FileWriter(archivo, false);

            File archivo2 = new File("Errores.txt");
            FileWriter archi = new FileWriter(archivo2, false);

            //escribir.write(Error);
            escribir.write("Núm    ");
            escribir.write("Dato Objeto    ");
            escribir.write("Dato Fuente   ");
            escribir.write("Dato Operador   \r\n");

            archi.write("Linea  ");
            archi.write("Error  \r\n");

            for (int u = 0; u < conttrip; u++) {
                escribir.write(tablatripletas[u][0] + "   ");
                escribir.write(tablatripletas[u][1] + "    ");
                escribir.write(tablatripletas[u][2] + "    ");
                escribir.write(tablatripletas[u][3] + "  \r\n");
            }

            for (int q = 0; q < conterrores; q++) {
                archi.write(tablaerrores[q][0] + "     ");
                archi.write(tablaerrores[q][1] + "  \r\n");
            }

            JOptionPane.showMessageDialog(null, "Se crea bien el archivo");
            escribir.close();
            archi.close();
        } catch (Exception e) {
            System.out.println("Error al escribir");

            JOptionPane.showMessageDialog(null, "Error al generar el archivo");
        }
    }//GEN-LAST:event_BtnDesplegarMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //jTextArea1.setText("");
        this.dispose();
        GraphAnalizador.main(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphAnalizador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDesplegar;
    private javax.swing.JPanel PanelBoton;
    private javax.swing.JPanel PanelCodigo;
    private javax.swing.JPanel PanelErrores;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelSimbolos;
    private javax.swing.JPanel PanelTripletas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
