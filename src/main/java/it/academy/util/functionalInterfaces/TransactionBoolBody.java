package it.academy.util.functionalInterfaces;

@FunctionalInterface
public interface TransactionBoolBody {

    boolean execute() throws Exception;
}
