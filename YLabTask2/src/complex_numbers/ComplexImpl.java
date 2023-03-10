package complex_numbers;

import java.util.Objects;

public class ComplexImpl implements Complex {
    private double real;
    private double imaginary;

    public ComplexImpl(double real) {
        this.real = real;
        this.imaginary = 0;
    }

    public ComplexImpl(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    @Override
    public Complex add(Complex z) {
        ComplexImpl complex = (ComplexImpl) z;
        return new ComplexImpl(this.real + complex.getReal(), this.imaginary + complex.getImaginary());
    }

    @Override
    public Complex subtract(Complex z) {
        ComplexImpl complex = (ComplexImpl) z;
        return new ComplexImpl(this.real - complex.getReal(), this.imaginary - complex.getImaginary());
    }

    @Override
    public Complex multiply(Complex z) {
        ComplexImpl complex = (ComplexImpl) z;
        if (this.imaginary == 0d || complex.getImaginary() == 0d) {
            return new ComplexImpl(this.real * complex.getReal());
        }
        return new ComplexImpl((this.real * complex.getReal()) - (this.imaginary * complex.getImaginary()),
                (this.real * complex.getImaginary()) + (this.imaginary * complex.getReal()));
    }

    @Override
    public double abs() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexImpl complex = (ComplexImpl) o;
        return Double.compare(complex.real, real) == 0 && Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public String toString() {
        return "Complex{" + real + " + " + imaginary + "i" + '}';
    }
}
