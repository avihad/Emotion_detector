package idc.cv.emotiondetector.utillities;

public abstract class Optional<E>
{
    public static <E> Optional<E> absent()
    {
        return new Absent<E>();
    }

    public static <E> Optional<E> of(E value)
    {
        return new Present<E>(value);
    }

    public abstract boolean isPresent();
    public abstract E get();

    public static class Present<E> extends Optional<E>
    {
        private final E value;

        private Present(E value)
        {
            this.value = value;
        }

        @Override
        public boolean isPresent()
        {
            return Boolean.TRUE;
        }

        @Override
        public E get()
        {
            return value;
        }
    }

    public static class Absent<E> extends Optional<E>
    {
        @Override
        public boolean isPresent()
        {
            return Boolean.FALSE;
        }

        @Override
        public E get()
        {
            throw new IllegalArgumentException("Value is absent");
        }
    }
}
