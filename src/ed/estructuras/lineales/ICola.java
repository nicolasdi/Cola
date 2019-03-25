
package ed.estructuras.lineales;

import java.util.Collection;

public interface ICola<E> extends Collection<E> {

	/**
	 * Elimina y devuelve al elemento que está al inicio de la cola.
	 * @return al elemento que se encuentra al inicio de la cola,
	 * {@code null} si la pila está vacía
	 */
	public E atiende();

	/**
	 * Agrega un elemento al final de la cola.
	 * @param e elemento por agregar al final  de la cola.
	 */
	public void forma(E e);

	/**
	 * Devuelve al elemento que  está al inicio de la cola.
	 * @return al elemento que se encuentra al inicio de la cola,
	 * {@code null} si la cola está vacía.
	 */
	public E mira();



}
