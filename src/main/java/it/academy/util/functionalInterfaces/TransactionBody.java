package it.academy.util.functionalInterfaces;

@FunctionalInterface
public interface TransactionBody {

    void execute() throws Exception;
}
