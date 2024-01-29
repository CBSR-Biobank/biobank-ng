package edu.ualberta.med.biobank.util;

import java.util.Optional;
import java.util.function.Supplier;
import io.jbock.util.Either;

public class EitherUtils {

    private EitherUtils() {
        throw new AssertionError();
    }

    public static <L, R>  Either<L, R> of(Optional<R> possibleValue, Supplier<L> errorSupplier) {
        return possibleValue.map(Either::<L, R>right).orElseGet(() -> Either.<L, R>left(errorSupplier.get()));
    }

}
