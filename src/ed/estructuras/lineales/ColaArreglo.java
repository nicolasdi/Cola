package ed.estructuras.lineales;

import ed.estructuras.ColeccionAbstracta;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Clase que implementa la estructura de datos Cola utilizando arreglos.
 */
/*
 * Características de *esta* cola:
 * Su tamaño inicial es 20 a menos que se indique en el
 * constructor un tamaño inicial.
 * La forma más segura de verificar que la cola está
 * vacía es a través de su método isEmpty().
 * El único que aumenta el tamaño de la estructura es forma.
 * El único que disminuye el tamaño de la estructura es atiende.
 */
public class ColaArreglo<E> extends ColeccionAbstracta<E> implements ICola<E> {

    /* NO VOLVER A DECLARAR TAM */
    //int tam;

    /* Lugar donde se almacenan los elementos que están en la cola */
    private E[] fila;

    /* Posición del arreglo donde se encuentra el elemento
     * próximo a salir de la cola */
    private int cabeza;

    /* Posición del arreglo donde se encuentra el último elemento que
     * ingresó a la cola */
    private int rabo;

    /* Provisión del tipo del arreglo para, en caso de ser necesario,
     * poder crear más arreglos del tipo E */
    private E[] semilla;

    /**
     * Inicializa una cola con capacidad para 20 elementos.
     * @param semilla arreglo vacío que nos indica el tipo de los elementos
     * que guardará la cola
     */
    public ColaArreglo(E[] semilla) {
        this(semilla,20);
    }

    /**
     * Inicializa una cola con la capacidad indicada.
     * @param semilla arreglo vacío del tipo de los elementos
     * que almacenará la cola
     * @param tamInicial capacidad inicial de la cola
     */
    public ColaArreglo(E[] semilla, int tamInicial) {
        if(semilla.length != 0) {
            throw new IllegalArgumentException("El arreglo debería ser vacío");
        }

        this.semilla = semilla;
        this.fila = Arrays.copyOf(semilla, tamInicial);
    }

    /* Herencia de Collection */
    @Override
    public boolean add(E e) {
        this.forma(e);
        //A pesar de que se espera que forma no falle, a menos que e
        //no sea de tipo o subtipo de E, no es tan seguro devolver
        //true directamente pues forma SÍ podría fallar. Duda ¿se
        //debería poner dentro de un try-catch?
        return true;
    }

    @Override
    public E atiende() {
        //Dado que esta lista acepta elementos nulos, que
        //este método devuelva null, no significa que la lista está
        //vacía, pero es importante verificar que no esté vacía para
        //asegurar la consistencia de la estructura después de que se
        //termina de ejecutar este método.
        if(this.isEmpty()) return null;

        E atendido = this.fila[cabeza];
        this.fila[cabeza] = null;

        //Caso especial: no es necesario mover a la cabeza
        if(this.size() == 1) {
            this.tam--;
            return atendido;
        }

        this.tam--;
        this.cabeza = this.posicionSiguiente(cabeza);
        return atendido;
    }

    /* Hereda este método de Collection, en la implementación original
     * se utiliza el método .remove() del iterador de la clase, pero
     * esta estructura no permite su uso por tanto se
     * ideó una nueva forma de vaciar la estructura.
     */
    @Override
    public void clear() {
        while(!this.isEmpty()) this.atiende();
    }

    @Override
    public void forma(E e) {
        //Si la cola está vacía
        if(this.isEmpty()) {
            this.fila[rabo] = e;
            this.tam++;
            return;
        }

        //Si el arreglo ya está lleno
        if(this.posicionSiguiente(this.rabo) == this.cabeza) {
            E[] aux = Arrays.copyOf(semilla, this.tam * 2);
            int transeunte = this.cabeza;
            int i = 0;

            //Se copian los elementos a un arreglo de mayor tamaño
            while(i < this.size()) {
                aux[i] = this.fila[transeunte];
                i++;
                transeunte = this.posicionSiguiente(transeunte);
            }

            this.fila = aux;
            this.cabeza = 0;
            this.rabo = tam - 1;
            //this.forma(e);
        }
        //Si queda lugar en el arreglo
        this.rabo = this.posicionSiguiente(rabo);
        this.fila[this.rabo] = e;
        this.tam++;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterador();
    }

	@Override
    public E mira() {
        //La linea siguiente no es necesaria, por default si el
        //arreglo está vacío, se almacena null en todas sus entradas.
        if(this.isEmpty()) return null;

        return this.fila[cabeza];
    }

    /* Se calcula la posición del arreglo a donde se debería de ir si
     * nos moviésemos 1 lugar atrás en la cola */
    private int posicionSiguiente(int i) {
	    return (i + 1) % this.fila.length;
    }

    /* Métodos que hereda de Collection, pero esta estructura no los
     * soporta */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    /*
     * Notas:
     * No se debe implementar al método hasNext() utilizando como
     * condición el tamaño de la estructura y usando un contador; puede
     * pasar que al utilizar el iterador se eliminen elementos de la estructura
     * haciendo que el contador sea mayor que la cantidad de elementos que quedan
     * en la estructura. Luego hasNext() daría false cuando debería dar true

     * banderaInicio no es inútil; no es confiable inicializar a
     * transeunte = ColaArreglo.this.cabeza en el constructor; puede pasar que
     * se mande a llamar al constructor y luego se elimine un elemento de la cola,
     * esto provocaría que ahora transeunte apunte al lugar donde solía estar la
     * cabeza y cause errores al momento de iterar sobre la estructura, con esta
     * bandera se asegura que en la primera iteración, transeunte apunte al valor
     * que de hecho tiene la cabeza de la cola.

     * Observaciones sobre la implementación de la clase Iterador:
     * hasNext()
     * Se inicializa a transeunte en la cabeza de la estructura.
     * Caso 1: cabeza <= rabo
     * transeunte debe pertenecer al intervalo [cabeza,rabo]
     * Caso 2: rabo < cabeza
     * transeunte debe pertenecer [0, rabo] o [cabeza, fila.length]
     * Caso 0: Cola vacía
     * Si la cola es vacía, cabeza == rabo y se caería en el caso 1, pero esto
     * sería un error, por tanto se devuelve false inmediatamente.
     */
    private class Iterador implements Iterator<E> {
        private boolean banderaInicio;
        private int transeunte;

        public Iterador() {
            banderaInicio = true;
        }

        //para asegurar que se empieza desde la cabeza de la cola
        @Override
        public boolean hasNext() {
            if(banderaInicio == true) {
                transeunte = ColaArreglo.this.cabeza;
                banderaInicio = false;
            }

            if(ColaArreglo.this.isEmpty()) return false;

            if(ColaArreglo.this.cabeza <= ColaArreglo.this.rabo) {
                return ColaArreglo.this.cabeza <= transeunte
                    && transeunte <= ColaArreglo.this.rabo;
            }

            return transeunte <= ColaArreglo.this.rabo
                || ColaArreglo.this.cabeza <= transeunte;
        }

        @Override
        public E next() {
            if(!this.hasNext()) {
                throw new IllegalStateException("No hay elemento siguiente");
            }

            E aux = fila[transeunte];
            transeunte = ColaArreglo.this.posicionSiguiente(transeunte);
            return aux;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
