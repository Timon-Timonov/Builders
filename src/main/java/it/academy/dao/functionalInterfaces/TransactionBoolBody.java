package it.academy.dao.functionalInterfaces;

@FunctionalInterface
public interface TransactionBoolBody {

    boolean execute() throws Exception;
}
