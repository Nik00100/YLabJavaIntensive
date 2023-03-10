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
        System.out.println(1);
        int prevStep = 3;
        int prevNum = 1;
        for (int i = 1; i < n; i++) {
            System.out.println(prevStep + prevNum);
            prevNum += prevStep;
            prevStep += 2;
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
        System.out.println(1);
        int prevStep = 3;
        int prevNum = 1;
        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                System.out.println(prevStep + prevNum);
            } else {
                System.out.println((-1) * (prevStep + prevNum));
            }
            prevNum += prevStep;
            prevStep += 2;
        }
    }

    @Override
    public void h(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.println(i);
            System.out.println(0);
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
