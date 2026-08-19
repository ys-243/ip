# UI Test Plan

## Test setup

- Application: Friday console chatbot
- Working directory: repository root
- Java version: 25 (`sdk use java 25.0.3.fx-zulu` on macOS)
- Compile command: `javac -d out src/main/java/*.java` (verified with Java 25.0.3 active)
- Launch command: `java -cp out Friday`
- Starting state: Fresh program launch; tasks are stored only in memory.
- Comparison: Compare exact output after each input. Normalize only CRLF to LF and one terminal-added echo of the submitted input unless a test case explicitly states another rule.
- Failure behavior: Stop on the first mismatch, terminate the program, and do not run later commands or cases.

## Test cases

Add cases in execution order using this structure.

### UI-001: Todo and unknown commands preserve the task list

**Aim:** Verify that empty and unknown commands are rejected without adding tasks, while valid todos still work.

**Starting state:** Fresh program launch.

#### Step 1

**Input**

```text
todo buy milk
```

**Expected output**

```text
____________________________________________________________
okay okay, i add buy milk to the list lor.
you have 1 tasks lah.
____________________________________________________________
```

#### Step 2

**Input**

```text
todo
```

**Expected output**

```text
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
```

#### Step 3

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][ ] buy milk
____________________________________________________________
```

#### Step 4

**Input**

```text
blah
```

**Expected output**

```text
____________________________________________________________
OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
```

#### Step 5

**Input**

```text
todo return book
```

**Expected output**

```text
____________________________________________________________
okay okay, i add return book to the list lor.
you have 2 tasks lah.
____________________________________________________________
```

#### Step 6

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][ ] buy milk
2.[T][ ] return book
____________________________________________________________
```

### UI-002: Deadline validation preserves task count and order

**Aim:** Verify that missing deadline fields do not add partial tasks and that a valid deadline is stored correctly.

**Starting state:** Fresh program launch.

#### Step 1

**Input**

```text
todo read book
```

**Expected output**

```text
____________________________________________________________
okay okay, i add read book to the list lor.
you have 1 tasks lah.
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline submit report
```

**Expected output**

```text
____________________________________________________________
OOPS!!! Please specify the deadline as: deadline DESCRIPTION /by DATE
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline submit report /by Friday
```

**Expected output**

```text
____________________________________________________________
Remember to finish hor: 
[D][ ] submit report(by: Friday)
you have 2 tasks lah.
____________________________________________________________
```

#### Step 4

**Input**

```text
deadline /by Friday
```

**Expected output**

```text
____________________________________________________________
OOPS!!! A deadline's description and date cannot be empty.
____________________________________________________________
```

#### Step 5

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][ ] read book
2.[D][ ] submit report(by: Friday)
____________________________________________________________
```

### UI-003: Event validation preserves task count and order

**Aim:** Verify that malformed or incomplete events are rejected and a valid event is stored correctly.

**Starting state:** Fresh program launch.

#### Step 1

**Input**

```text
event project meeting /from 2pm /to 3pm
```

**Expected output**

```text
____________________________________________________________
orh, don't forget to attend ah: 
[E][ ] project meeting(from: 2pm to: 3pm)
you have 1 tasks lah.
____________________________________________________________
```

#### Step 2

**Input**

```text
event lunch /from 12pm
```

**Expected output**

```text
____________________________________________________________
OOPS!!! Please specify the event as: event DESCRIPTION /from START /to END
____________________________________________________________
```

#### Step 3

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[E][ ] project meeting(from: 2pm to: 3pm)
____________________________________________________________
```

#### Step 4

**Input**

```text
event /from 2pm /to 3pm
```

**Expected output**

```text
____________________________________________________________
OOPS!!! An event's description, start, and end cannot be empty.
____________________________________________________________
```

#### Step 5

**Input**

```text
event dinner /from 7pm /to 8pm
```

**Expected output**

```text
____________________________________________________________
orh, don't forget to attend ah: 
[E][ ] dinner(from: 7pm to: 8pm)
you have 2 tasks lah.
____________________________________________________________
```

#### Step 6

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[E][ ] project meeting(from: 2pm to: 3pm)
2.[E][ ] dinner(from: 7pm to: 8pm)
____________________________________________________________
```

### UI-004: Mark and unmark validation preserves completion state

**Aim:** Verify that missing, nonnumeric, and out-of-range task numbers do not change completion state.

**Starting state:** Fresh program launch.

#### Step 1

**Input**

```text
todo submit assignment
```

**Expected output**

```text
____________________________________________________________
okay okay, i add submit assignment to the list lor.
you have 1 tasks lah.
____________________________________________________________
```

#### Step 2

**Input**

```text
mark
```

**Expected output**

```text
____________________________________________________________
OOPS!!! Please specify a task number.
____________________________________________________________
```

#### Step 3

**Input**

```text
mark one
```

**Expected output**

```text
____________________________________________________________
OOPS!!! The task number must be a whole number.
____________________________________________________________
```

#### Step 4

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][ ] submit assignment
____________________________________________________________
```

#### Step 5

**Input**

```text
mark 1
```

**Expected output**

```text
Good! This task done liao: 
[T][X] submit assignment
____________________________________________________________
```

#### Step 6

**Input**

```text
unmark 2
```

**Expected output**

```text
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
```

#### Step 7

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][X] submit assignment
____________________________________________________________
```

#### Step 8

**Input**

```text
unmark 1
```

**Expected output**

```text
Nevermind! Can do later: 
[T][ ] submit assignment
____________________________________________________________
```

#### Step 9

**Input**

```text
list
```

**Expected output**

```text
Here are your tasks:
1.[T][ ] submit assignment
____________________________________________________________
```
