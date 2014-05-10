package com.base.util;

public final class Fraction extends Number implements Comparable<Fraction> {
    private final long numerator;
    private final long denominator;

    public Fraction(final long numerator, final long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(final long numerator) {
        this(numerator,1);
    }

    public Fraction plus(final Fraction f){
        return new Fraction(this.numerator*f.denominator+this.denominator*f.numerator,this.denominator*f.denominator);
    }

    public Fraction minus(final Fraction f){
        return this.plus(f.negate());
    }

    public Fraction times(final Fraction f){
        return new Fraction(this.numerator*f.numerator,this.denominator*f.denominator);
    }

    public Fraction divideBy(final Fraction f){
        return this.times(f.invert());
    }

    public Fraction invert(){
        return new Fraction(this.denominator,this.numerator);
    }

    public Fraction negate(){
        return new Fraction(-this.numerator,this.denominator);
    }

    public Fraction reduce(){
        long factor = gcd(Math.max(this.numerator, this.denominator), Math.min(this.numerator, this.denominator));
        return new Fraction(this.numerator/factor,this.denominator/factor);
    }

    private static long gcd(final long a, final long b) {
        if(b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    public double doubleValue() {
        return ((double)this.numerator) / ((double)this.denominator);
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public int intValue() {
        return (int) longValue();
    }

    public long longValue() {
        return (this.numerator) / (this.denominator);
    }

    public int compareTo(final Fraction o) {
        return this.minus(o).intValue();
    }
}
