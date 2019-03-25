package ed.estructuras;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Clase que implementa la interfaz Collection.
 * Una colección cuenta con un numero definido de elementos.
 * @param <E> tipo de datos que guardará la estructura.
 */

/*
 *** Características de *esta* colección:
 * - Permite elementos nulos
 * - Permite elementos repetidos
 * - Los elementos no necesariamente cumplen con un criterio de orden
 *** Detalles de la Implementación
 * - Para métodos que modifican a la colección. El tamaño de la
 *   colección sólo es modificada por el iterador y el método add
 */
public abstract class ColeccionAbstracta<E> implements Collection<E> {

    /** Cantidad de elementos que tiene la estructura. */
    protected int tam = 0;

    /**
     * Agrega todos los elementos de la colección c a la colección que
     * manda a llamar el método.
     * @param c colección con elementos por agregar. La colección se
     * agrega si es distinta de {@code null} y c no es *esta* colección.
     * @return <tt>true</tt> si se agregó al menos un elemento a la
     * colección, <tt>false</tt> en otro caso.
     * @throws NullPointerException si la colección que se intenta
     * agregar es nula.
     * @throws UnsupportedOperationException si la
     * estructura no permite esta operación.
     * @throws ClassCastException si algún elemento de la colección
     * no puede ser tipo {@code <E>}
     * @throws IllegalArgumentException si se intenta agregar a this.
     * @throws IllegalStateException si no todos los elementos pueden ser
     * agregados debido al tamaño de la estructura.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c == null) {
            throw new NullPointerException();
        }

        if(c == this) {
            throw new IllegalArgumentException();
        }

        boolean coleccionModificada = false;

        // Se puede hacer que elemento sea de tipo E
        // porque ? extiende a E
        for(E elemento : c) {
            this.add(elemento);
            //consideramos que el método add aumenta el tamaño de la estructura
            //this.tam++;
            coleccionModificada = true;

        }

        return coleccionModificada;
    }

    /**
     * Elimina todos los elementos de la estructura.
     * @throws UnsupportedOperationException si esta
     * estructura no permite esta operación.
     */
    @Override
    public void clear() {
        // No se utilizó for-each porque no es seguro con remove() adentro.
        Iterator<E> transeunte = this.iterator();
        while(transeunte.hasNext()) {
            transeunte.next();
            transeunte.remove();
            //Consideramos que remove disminuye el tamaño
            //this.tam--
        }
    }

    /**
     * Verifica si esta colección contiene al objeto o.
     * @param o objeto a buscar en la estructura.
     * @return <tt>true</tt> si el objeto aparece al menos una vez en
     * esta colección.
     */
    @Override
    public boolean contains(Object o) {
        for(E elemento : this) {
            if(elemento == null && o == null) return true;
            if(elemento == null) continue;
            if(elemento.equals(o)) return true;
        }

        return false;
    }

    /**
     * Verifica si esta colección contiene todos los elementos de la
     * colección c.
     * @param c colección de la cual se va a verificar la
     * contención. Solo se verifica la contención si c es distinto de
     * {@code null}
     * @return <tt>true</tt> si la colección contiene todos los
     * elementos de la colección c, <tt>false</tt> en otro caso.
     * @throws NullPointerException si la colección c es null
     */
    @Override
    /* c es subconjunto de this **Cuidado porque permite repetidos */
    public boolean containsAll(Collection<?> c) {
        if(c == null) {
            throw new NullPointerException();
        }
        // Si elemento es null, es válido hacer la asignación, pues
        // null es un valor válido para object
        for(Object elemento : c) {
            if(!this.contains(elemento)) return false;
        }
        return true;
    }

    /**
     * Nos dice si dos objetos son iguales. El criterio de igualdad
     * se cumple si dos estructuras tienen a los mismos elementos y
     * están en la misma posición.
     * @param o el objeto con el cual se va a comparar.
     * @return <tt>true</tt> si los objetos son iguales, <tt>false</tt> si no.
     */
    @Override
    //Revisar, todavía podría haber casos que no estoy considerando
    public boolean equals(Object o) {
        if(o == this) return true;

        if(!(o instanceof Collection<?>)) return false;

        Collection<?> objetoEquivalente = (Collection<?>) o;

        if(this.size() != objetoEquivalente.size()) return false;

        Iterator<E> checador = this.iterator();
        Iterator<?> checador2 = objetoEquivalente.iterator();

        while(checador.hasNext()) {
            E elemento = checador.next();
            Object elemento2 = checador2.next();
            if(elemento == null) {
                if(elemento2 != null) return false;
                continue;
            }

            if(!elemento.equals(elemento)) return false;
        }
        return true;
    }

    /**
     * Nos da un código identificador único para cada objeto.
     * @return identificador el código con el cual el objeto se
     * identifica.
     */
    @Override
    /* El hashcode de nuestra colección es suma del hash de cada uno
     * de los objetos que contiene */
    /* El hash no debe utilizarse para verificar igualdad, es verdad
     * que si son iguales tienen el mismo hash, pero no al revés */
    public int hashCode() {
        int identificador = 0;
        for(E elemento: this) {
            if(elemento == null) continue;
            identificador += elemento.hashCode();
        }
        return identificador;
    }

    /**
     * Nos dice si la colección no contiene elementos.
     * @return <tt>true</tt> si la colección es vacía, <tt>false</tt>
     * en otro caso.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Elimina en la colección (si existe) sólo la primera instancia
     * del elemento especificado.
     * @param o el objeto a eliminar (si existe) en la colección.
     * @return <tt>true</tt> si se elimina una instancia de la
     * colección, <tt>false</tt> si no.
     * @throws UnsupportedOperationException si la estructura no permite
     * esta operación.
     */
    @Override
    //No se puede usar for-each pues se utiliza .remove()
    public boolean remove(Object o) {
        Iterator<E> transeunte = this.iterator();
        while(transeunte.hasNext()) {
            Object elemento = transeunte.next();
            if(elemento == null && o == null) {
                transeunte.remove();
                return true;
                //el control del tamaño de la colección está en el iterador
                //this.tam--;
            }

            if(elemento == null) {
                continue;
            }

            if(elemento.equals(o)) {
                transeunte.remove();
                return true;
            }
        }
        return false;
    }
    /**
     * Elimina todas las ocurrencias de la colección, que estén
     * contenidas en la colección c.
     * @param c la colección que indica qué elementos se eliminarán de
     * la colección que manda llamar al método.
     * @return <tt>true</tt> si al menos un elemento de la colección
     * fue eliminado, <tt>false</tt> en otro caso.
     * @throws UnsupportedOperationException si la estructura no permite esta
     * operación.
     * @throws ClassCastException si algún elemento de la colección c es
     * incompatible con esta colección.
     * @throws NullPointerException si la colección c es null.
     */
    @Override
    /* Diferencia de conjuntos A\B */
    public boolean removeAll(Collection<?> c) {
        if(c == null) {
            throw new NullPointerException();
        }

        boolean coleccionModificada = false;
        Iterator<E> checador = this.iterator();

        while(checador.hasNext()) {
            if(c.contains(checador.next())) {
                checador.remove();
                coleccionModificada = true;
            }
        }

        return coleccionModificada;
    }

    /**
     * Al finalizar este método, la colección sólo conserva los
     * elementos que tiene en común con la colección c.
     * @param c coleccion tal que será referencia para decidir qué
     * elementos permanecen en la colección.
     * @return <tt>true</tt> si se eliminó al menos un elemento de la
     * colección que manda a llamar al método, <tt>false</tt> en otro
     * caso.
     * @throws UnsupportedOperationException si esta estructura no permite
     * la operación
     * @throws ClassCastException si algún elemento de *esta* colección
     * es incompatible con el de la colección c.
     * @throws NullPointerException si la colección c es null.
     */
    @Override
    /* Interseccion de conjuntos */
    /* No cambiar con estructura de for-each porque es inseguro usar
     * .remove() adentro*/
    public boolean retainAll(Collection<?> c) {
        if(c == null) {
            throw new NullPointerException();
        }

        boolean coleccionModificada = false;
        //si c es igual a la colección (A intersección A) = A
        // y la colección no se modifica
        if(c == this) {
            return coleccionModificada;
        }

        Iterator<E> checador = this.iterator();

        while(checador.hasNext()) {
            if(!(c.contains(checador.next()))) {
                checador.remove();
                coleccionModificada = true;
            }
        }
        return coleccionModificada;
    }

    /**
     * Indica la cantidad de elementos de nuestra colección.
     * @return cantidad de elementos que contiene la colección.
     */
    @Override
    public int size() {
        return tam;
    }

    /**
     * Entrega un arreglo que contiene todos los elementos que existen
     * actualmente en la colección y en el mismo orden en que están
     * contenidos en ésta.
     * @return arreglo con los elementos actuales de la
     * colección.
     */
    @Override
    /* El acomodo de los elementos en este arreglo no necesariamente cumplen con un criterio de orden */
    public Object[] toArray() {
        Object[] representacion = new Object[this.size()];
        int contador = 0;

        for(Object elemento : this) {
            representacion[contador] = elemento;
            contador++;
        }

        return representacion;
    }

    /**
     * Entrega un arreglo de tipo T que contiene todos los elementos
     * que existen actualmente en la colección y en el mismo orden en
     * que están contenidos en la coleccion.
     * @param a arreglo el cual indica el tipo y además en el cual(es es de tamaño adecuado) se guardan los elementos de la colección
     * @return arreglo con los elementos actuales de la
     * colección
     * @throws ArrayStoreException si el tipo del arreglo T[] no es un super
     * tipo de E (los elementos almacenados en esta colección).
     * @throws NullPointerException si el arreglo especificado es null.
     */
    @Override
    /* Los elementos en el arreglo no necesariamente cumplen con un
     * criterio de orden */
    public <T> T[] toArray(T[] a) {
        if(a == null) {
            throw new NullPointerException("El arreglo no puede ser nulo");
        }

        int nvoTam = a.length >= this.size() ? a.length : this.size();

        T[] contenedor = Arrays.copyOf(a, nvoTam);
        int contador = 0;
        for(E elemento: this) {
            T aux = (T) elemento;
            contenedor[contador] = aux;
            contador++;
        }

        return contenedor;
    }

    /**
     * @return una representación de la colección.
     */
    @Override
    public String toString() {
        StringBuilder representacion = new StringBuilder();
        for(E elemento : this) {
            //se manda a llamar en automático al método toString aún
            //si el elemento es null
            representacion.append(elemento + " ,");
        }
        return representacion.toString();
    }

    /**
     * Se asegura de agregar al elemento e a la colección.
     * @param e elemento que se desea agregar a la colección.
     * @return <tt>true</tt> si se agregó e a la colección,
     * <tt>false</tt> en otro caso.
     * @throws UnsupportedOperationException si la estructura
     * no permite la operación
     * @throws ClassCastException si el tipo de e no es compatible
     * con E.
     * @throws IllegalArgumentException
     * @throws IllegalStateException si el elemento no puede ser agregado
     * debido a alguna restricción de la implementación del
     * método en la estructura.
     */
    public abstract boolean add(E e);

    /**
     * Entrega un iterador que funciona sobre esta estructura.
     * @return un iterador para esta estructura.
     */
    public abstract Iterator<E> iterator();
}
