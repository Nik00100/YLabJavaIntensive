package sequences;

public class SequencesImpl implements Sequences {
    @Override
    public void a(int n) {
        for (int i = 1; i <= n; i++)
            System.out.println(2 * i);
    }

    @Override
    public void b(int n) {
        int prevNum = 1;
        for (int i = 1; i <= n; i++) {
            System.out.println(prevNum);
            prevNum += 2;
        }
    }

    @Override
    public void c(int n) {
        for (int i = 1; i < n; i++) {
            System.out.println(i * i);
        }
    }

    @Override
    public void d(int n) {
        for (int i = 1; i <= n; i++)
            System.out.println(i * i * i);
    }

    @Override
    public void e(int n) {
        for (int i = 0; i < n; i++)
            System.out.println((int) Math.pow(-1, i));
    }

    @Override
    public void f(int n) {
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                System.out.println((-1) * i);
            } else {
                System.out.println(i);
            }
        }
    }

    @Override
    public void g(int n) {
        for (int i = 1; i < n; i++) {
            int num = i * i;
            if (i % 2 == 0) {
                System.out.println((-1) * num);
            } else {
                System.out.println(num);
            }
        }
    }

    @Override
    public void h(int n) {
        int prev = 1;
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                System.out.println(0);
            } else {
                System.out.println(prev);
                prev++;
            }
        }
    }

    @Override
    public void i(int n) {
        int prev = 1;
        for (int i = 1; i <= n; i++) {
            System.out.println(i * prev);
            prev *= i;
        }
    }

    @Override
    public void j(int n) { // fibonacci
        if (n < 3) {
            for (int i = 1; i <= n; i++)
                System.out.println(1);
        } else {
            int prev1 = 1;
            int prev2 = 1;
            System.out.println(1);
            System.out.println(1);
            for (int i = 3; i <= n; i++) {
                int temp = prev1 + prev2;
                System.out.println(temp);
                prev1 = prev2;
                prev2 = temp;
            }
        }
    }
}
