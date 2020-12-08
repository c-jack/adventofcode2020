# [Advent of Code (2020)](https://adventofcode.com/)

Here's my little application written as part of 2020's [Advent of Code](https://adventofcode.com/) challenges.

I'll be adding to this repository as each challenge is completed, and I'll be aiming to create it using **Java 8**

Note: one of the great things about the Challenge is that each entrant appears to get their own variation of the
dataset. This means that the 'answer' is technically the solution, and not the answer itself!

---

### My submissions

I'm only intending on submitting answers to the  [Advent of Code](https://adventofcode.com/) site once my solutions are
technically viable; that is, no 'guessing' or using answers that haven't been provided by my code, and I'm not going to
submit an answer until the code is clean and maintainable _(but not fully relying on administrative bits like JavaDoc
and miscellaneous non-functional bits)_.

### My 'Pattern'

Each 'Answer' will be buffered by some tests that are enforced
by [`assertions`](https://docs.oracle.com/javase/7/docs/technotes/guides/language/assert.html) - these aren't widely
used in my experience, so it gives me a change to practice using them to good effect *(a VM argument
of `-enableassertions` is required to enable them)*.

> My thinking: While assertions are normally used in Unit Tests (for example, JUnit or Hamcrest), these form part of the build or CI process - by using assertions in the application logic, I can be sure that the application limits the effect of broken logic at run time

Each test class contains a `testLogic()` method at the end. The data and answers are based on the verifiable examples in
each of the AOC challenges (where provided).

I'm also intending on making my solutions capable of wider implementation - that is, imagining that each solution is
potentially a Solution that should be able to accept future data and/or modifications. This means utilising Objects
where it is suitable to do so, instead of anonymous Arrays, Lists and other such objects.

**Rather importantly**, my solutions _should be maintainable_ - that is, coding as cleanly as possible, and leaving
sufficient JavaDoc and notes to ensure that I would still be able to efficiently extend or maintain the solution if I
became completely unfamiliar with it (say, in a years time).  
I'll be attempting to stick to DRY (don't repeat yourself) as much as possible, with module methods where it's efficient
to do so.

### A caveat or two

Concessions have made been that aren't really 'good designs', such as using `System.out.println`. This is purely because
it's far more readable in the Console for debugging than `Logger`!

Some of the `println` stuff has been left in for demonstration, so there's some intentionally-commented-out-code left
in.

Last but not least, I'll intend to use some design patterns that are not necessarily the most optimal, but are a good
excuse to use a variety of 'Chapter 1' Java methods that I don't always get an excuse to use.