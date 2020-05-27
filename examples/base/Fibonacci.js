function fib(N) {
    if (N < 2) {
        return 1;
    }
    return fib(N - 1) + fib(N - 2);
}

console.log(fib(40));
