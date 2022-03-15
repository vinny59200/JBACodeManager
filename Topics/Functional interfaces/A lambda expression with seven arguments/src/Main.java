class Seven {
    public static SeptenaryStringFunction fun = (x, y, z, x1, y1, z1, x2) -> (x + y + z + x1 + y1 + z1 + x2).toUpperCase();
    //write your code here
}

@FunctionalInterface
interface SeptenaryStringFunction {
    String apply(String s1, String s2, String s3, String s4, String s5, String s6, String s7);
}