
package ed.estructuras.lineales;

import ed.estructuras.ColeccionAbstracta;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import ed.estructuras.lineales.*;

/**
 * Clase que implementa la estructura de datos Cola utilizando arreglos
 */
/*
 * Características de *esta* cola:
 * Su tamaño inicial es 20 a menos que se indique en el constructor un tamaño inicial.
 * La forma más segura de verificar que la cola está
 * vacía es a través de su método isEmpty().
 */
public class ColaArreglo<E> extends ColeccionAbstracta<E> implements ICola<E> {

    /* Cantidad actual de elementos formados en la cola */
    //int tam;

    /* Lugar donde se almacenan los elementos en la cola */
    private E[] fila;

    /* Posición del arreglo donde se encuentra el elemento
     * siguiente a salir de la cola */
    private int cabeza;

    /* Posición del arreglo donde se encuentra el último elemento que
     * ingresó a la cola */
    private int rabo;

    /* Provisión del tipo del arreglo para, en caso de ser necesario,
     * poder crear más arreglos del tipo E */
    private E[] semilla;

    public ColaArreglo(E[] semilla) {
        if(semilla.length != 0) {
            throw new IllegalArgumentException("El arreglo debería ser vacío");
        }

        this.semilla = semilla;
        this.fila = Arrays.copyOf(semilla, 20);
    }

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
        return true;
    }

    public E atiende() {
	    //Dado que esta lista acepta elementos nulos, que
	    //este método devuelva null, no significa que la lista está
	    //vacía, pero es importante verificar que no esté vacía para
	    //asegurar la consistencia de la estructura después de que se
	    //termina de ejecutar este método.
	    if(this.isEmpty()) return null;

	    E atendido = this.fila[cabeza];
        this.fila[cabeza] = null;

        if(this.size() == 1) {
	        this.tam--;
	        return atendido;
	    }

        this.tam--;
        this.cabeza = this.posicionSiguiente(cabeza);
        return atendido;
    }

    private int posicionSiguiente(int i) {
        //Se calcula la posición del arreglo a donde se debería de ir
        //si nos moviésemos 1 lugar atrás en la cola
        return i = (i + 1) % this.fila.length;
    }

	@Override
	public void clear() {
		while(!this.isEmpty()) this.atiende();
	}

    public void forma(E e) {
	    //Si la cola está vacía
	    if(this.isEmpty()) {
		    this.fila[rabo] = e;
		    this.tam++;
		    return;
        }

        //Si el arreglo ya está lleno
        if(this.posicionSiguiente(this.rabo) == this.cabeza){
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
            this.rabo = tam -1 ;
            this.forma(e);
        }
        //Si queda lugar en el arreglo
        this.rabo = this.posicionSiguiente(rabo);
        this.fila[this.rabo] = e;
        this.tam++;
    }

    public Iterator<E> iterator() {
        return new Iterador();
    }

    public E mira() {
	    //La linea siguiente no es necesaria, por default si el
	    //arreglo está vacío, se almacena null en todas sus entradas.
	    if(this.isEmpty()) return null;

        return this.fila[cabeza];
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

    private class Iterador implements Iterator<E> {
	    private boolean banderaInicio;
	    private int transeunte = 0;
	    private int i = 0;
	    public Iterador() {
	    banderaInicio = true;
	    transeunte = 0;
	    }

	    //para asegurar que se empieza desde la cabeza de la cola
	    @Override
        public boolean hasNext() {
		    if(banderaInicio == true) {
			    transeunte = ColaArreglo.this.cabeza;
			    banderaInicio = false;
		    }
		    return i < ColaArreglo.this.size();
        }

        @Override
        public E next() {
	        if(!this.hasNext()) {
		        throw new IllegalStateException("No hay elemento siguiente");
	        }

	        E aux = fila[transeunte];
	        transeunte = ColaArreglo.this.posicionSiguiente(transeunte);
	        i++;
	        return aux;
        }

        @Override
        public void remove() {
	        throw new UnsupportedOperationException();
        }
    }

	/*
	  Observamos que si se inicializara transeunte en
	  ColaArreglo.this.cabeza y sólo se utilizara, podría pasar
	  que alguien manda a llamar al constructor de la clase
	  Iterador, puede pasar además que se mande a llamar a el
	  método atiende() Observemos que se modifica la cabeza de la
	  estructura, pero transeunte en el objeto seguiría apuntando
	  a la vieja cabeza, por tanto para segurar que el iterador
	  siempre inicie en la cabeza, es útil banderaInicio

	*/
}
