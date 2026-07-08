# Prompt Writing Guide — From Basic to Pro

> For: HEANG (Spring Boot developer, new to AI prompting)
> Goal: Save time, save tokens, get better answers from AI

---

## The 3-Part Rule (Always Start Here)

Every good prompt needs these 3 parts:

| Part | Question | Example |
|---|---|---|
| **Context** | Who are you + what situation? | "I am a Spring Boot developer building JWT auth" |
| **Task** | What exactly do you want? | "Explain line 49 in JWTAuthenticationFilter.java" |
| **Constraint** | How do you want the answer? | "5 sentences, no code, beginner level" |

**Missing any part = bad result.**

---

## Technique 1 — Be Specific About Your Level

Do NOT write: `"I am a Spring developer"`

DO write: `"I know basic Spring Boot but I have never used SecurityContextHolder before"`

AI cannot guess your level on each specific topic.
Tell it exactly what you know and what you don't.

---

## Technique 2 — Point to Exact Location

Do NOT write: `"explain this code"`

DO write: `"In JWTAuthenticationFilter.java line 49, explain this line: Authentication authentication = SecurityContextHolder.getContext().getAuthentication();"`

Exact file + exact line + paste the code = AI answers the right thing.

---

## Technique 3 — Iterative Prompting (Build Understanding Step by Step)

Do not ask one big question. Use 4 small prompts:

```
Prompt 1 → What does this do? (basic understanding)
Prompt 2 → Go deeper on one specific part
Prompt 3 → "I think X is true. Is that correct?" (test yourself)
Prompt 4 → Ask for a real scenario or edge case
```

**Why:** One big prompt = one shallow answer. Four small prompts = deep understanding.

---

## Technique 4 — Constrain the Output

Always tell AI how to answer. Examples:

| What you want | What to write |
|---|---|
| Short answer | "Answer in 3 sentences max" |
| No code | "Explain only, no code examples" |
| Simple language | "Use simple English, I am not a native speaker" |
| Focused answer | "Only explain X, ignore Y and Z" |

**Why:** Without constraint, AI gives you everything. You waste time reading what you don't need.

---

## Technique 5 — Give AI the Codebase Context

When working in your project, always tell AI:
- The file name
- The line number
- Paste the exact code block

**Bad:**
> "Why do we check authentication == null?"

**Good:**
> "In JWTAuthenticationFilter.java line 51 in project spring_jwt_authentication:
> `if (userEmail != null && authentication == null)`
> Why do we check `authentication == null`? What happens if we remove it? One paragraph, no code."

---

## Technique 6 — Test Your Understanding (The "I Think" Prompt)

After AI explains something, always write:

> "I think [your understanding]. Is that correct? Just yes or no, then one sentence why."

**Why:** This forces you to think. It also shows AI where you misunderstood so it can correct only that part.

---

## Technique 7 — One Topic Per Prompt

Do NOT combine multiple questions in one prompt.

**Bad:**
> "Explain JWT, SecurityContextHolder, and how filters work in Spring"

**Good:**
> Three separate prompts, one topic each.

**Why:** AI tries to answer all of them at once. The answer is wide but shallow. One topic = focused and deep.

---

## Technique 8 — Tell AI What NOT To Do

Add a negative constraint when you want a focused answer:

- `"Do not show code"`
- `"Do not explain JWT basics, I already know that"`
- `"Do not give me a long history, just the technical answer"`

**Why:** Saves tokens. Saves your reading time. AI stops padding the answer.

---

## Technique 9 — Ask for One Example Only

Do NOT write: `"Give me examples"`

DO write: `"Give me ONE example using a bank transfer scenario"`

**Why:** AI gives 5 examples when you only need 1. Specify the domain (banking, payment, account) to make the example relevant to your work.

---

## Technique 10 — Role Prompting (Intermediate Level)

Tell AI what role to play:

> "Act as a strict senior Spring Boot developer reviewing my code. Do not rewrite it. Only point out what is wrong and ask me how I would fix it."

**Why:** The role changes how AI responds. A "senior reviewer" gives harder, more honest feedback than a default AI response.

---

## Quick Checklist (Use Before Every Prompt)

Before you send a prompt, check:

- [ ] Did I give Context? (who I am, what project, what I already know)
- [ ] Did I give a specific Task? (exact file, exact line, exact question)
- [ ] Did I give a Constraint? (length, format, level, what to exclude)
- [ ] Is this ONE topic only? (not mixing 3 questions)
- [ ] Did I paste the exact code or line I am asking about?

If any box is unchecked → fix the prompt before sending.

---

## Bad vs Good — Quick Reference

| Bad Prompt | Good Prompt |
|---|---|
| "Explain this code" | "In JWTAuthenticationFilter.java line 54, explain `jwtService.isTokenValid()`. 3 sentences, no code." |
| "How do indexes work?" | "I know basic SQL but never used indexes. Explain B-tree index with one banking example. Short answer." |
| "Fix my bug" | "I am a Spring Boot developer. In UserService.java line 32, I get NullPointerException on `user.getEmail()`. What is the root cause? Do not rewrite the code." |
| "What is JWT?" | "I use JWT in my project but I don't understand why we use `substring(7)` when extracting the token from Authorization header. One sentence explanation." |

---

## Token-Saving Tips

1. **Shorter context = fewer tokens.** Say your level in one sentence, not a paragraph.
2. **Paste only the relevant lines**, not the whole file.
3. **Use "no code" constraint** when you only need concepts.
4. **Ask follow-up prompts** instead of one giant prompt — each follow-up is smaller.
5. **"One sentence"** or **"3 sentences max"** are the most powerful constraints for saving tokens.

---

## Where You Are Now (2026-06-30)

| Technique | Status |
|---|---|
| 3-Part Rule (Context + Task + Constraint) | Practiced — improving |
| Specific level in Context | Practiced — improving |
| Iterative prompting (4-step) | Learned — needs more practice |
| "I Think" prompt | Not yet practiced |
| Role prompting | Not yet covered |
| Negative constraints | Not yet covered |

> Next: practice Technique 6 ("I Think" prompt) in your next session.
