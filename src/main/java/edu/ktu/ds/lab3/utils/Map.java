package edu.ktu.ds.lab3.utils;

public interface Map<K, V> {


    boolean isEmpty();

    int size();

    void clear();

    /**
     * Atvaizdis papildomas nauja pora.
     *
     * @param key   raktas,
     * @param value reikšmė.
     * @return Grąžinama atvaizdžio poros reikšmė.
     */
    V put(K key, V value);

    /**
     * Grąžinama atvaizdžio poros reikšmė.
     *
     * @param key raktas.
     * @return Grąžinama atvaizdžio poros reikšmė.
     */
    V get(K key);

    /**
     * Iš atvaizdžio pašalinama pora.
     *
     * @param key raktas.
     * @return Grąžinama pašalinta atvaizdžio poros reikšmė.
     */
    V remove(K key);

    boolean contains(K key);

    /**
     * Pakeičia atvaizdyje egzistuojantį raktą ir jį atitinkančią reikšmę naują reikšme ir grąžina true.
     * Jei raktas neegzistuoja atvaizdyje, ar jo reikšmė neatitinka metodo argumente nurodytos senosios reikšmės,
     * pakeitimas nevykdomas ir gražinama false.
     *
     * @param key      raktas.
     * @param oldValue sena reikšmė.
     * @param newValue nauja reikšmė.
     * @return true, jei pakeitimas įvyko
     */
    boolean replace(K key, V oldValue, V newValue);

    /**
     * Patikrinama ar atvaizdyje egzistuoja vienas ar daugiau raktų metodo argumente nurodytai reikšmei
     *
     * @param value reikšmė.
     * @return true, jei atvaizdyje egzistuoja vienas ar daugiau raktų metodo argumente nurodytai reikšmei
     */
    boolean containsValue(Object value);
}
