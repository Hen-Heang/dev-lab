# Spring AI — Learning Path (Basic to Advanced)
> Folder: `8. Spring AI`
> Prerequisite: comfortable building REST endpoints (section 2) and config/profiles (section 1, step 4).

---

## Why This Section?

LLM features are now a normal part of backend work, and Spring AI gives you the same clean abstraction style you already know (`ChatClient` feels like `RestTemplate`/`JdbcTemplate`). This is a high-signal skill for 2026 job applications — even one working AI endpoint on your résumé stands out.

> ⚠️ You need an API key (e.g. `OPENAI_API_KEY`). Set it as an environment variable — never commit it. Calls may cost money; check the provider's free tier.

---

## Project — `#1 spring-ai-getting-started`

Two controllers show two levels of control:
- `ChatController` — the simple, high-level `ChatClient` API
- `PromptController` — explicit `Prompt` / message construction
- `ChatClientConfig` — building and configuring the `ChatClient` bean

### Step 1 — Configure the model
**What to learn**
- `spring-ai-starter-model-openai` + the `spring-ai-bom` for version alignment
- Putting the API key + model name in config (env var → `application.yml`), NOT in code
- Building a `ChatClient` bean in `ChatClientConfig`

**Key concept**
```
Spring AI abstracts the provider. You program against ChatClient;
swapping OpenAI → Anthropic/Ollama is mostly a dependency + config change,
not a code rewrite. The BOM keeps all spring-ai modules on one version.
```

**Questions**
1. Why inject the API key from an env var instead of hardcoding it?
2. What does the `spring-ai-bom` do for you?
3. How would you switch to a different model provider?

### Step 2 — Simple chat (`ChatController`)
**What to learn**
- `chatClient.prompt().user(message).call().content()` — send a prompt, get a string back
- Wrapping it in a normal `@RestController` endpoint

**Questions**
1. What is the minimal call to get a text completion from `ChatClient`?
2. How do you turn the response into streaming (`Flux<String>`) instead of one blocking string?

**Practice exercise**
Add a `GET /chat/stream?message=...` endpoint that streams tokens as `text/event-stream`.

### Step 3 — Prompt control (`PromptController`)
**What to learn**
- Building a `Prompt` from a **system message** (sets behavior) + **user message** (the request)
- Prompt templates with variables
- Tuning options (temperature, max tokens) where supported

**Key concept**
```
System message = the assistant's role/rules ("You are a Khmer-English translator").
User message   = the actual request.
Templates let you inject variables safely instead of string-concatenating prompts.
```

**Questions**
1. What is the difference between a system message and a user message?
2. What does temperature control, and when do you want it low vs. high?
3. Why use a prompt template instead of string concatenation?

**Practice exercise**
Build a `/translate?text=...&to=Korean` endpoint using a system message that forces translation-only output (no explanations), with a templated user message.

---

## Where to Go Next (concepts to explore after the basics)

| Concept | What it adds |
|---|---|
| Structured output | Map the LLM response straight into a Java record/DTO |
| RAG (Retrieval-Augmented Generation) | Ground answers in YOUR data via a vector store + embeddings |
| Tool/function calling | Let the model call your Java methods |
| Memory / chat history | Multi-turn conversations |

---

## Summary Table

| Step | Focus | API | Priority |
|---|---|---|---|
| 1 | Configure model + key | ChatClientConfig, BOM | 🔥 |
| 2 | Simple chat | ChatClient (`.call()`) | 🔥 |
| 3 | Prompt control | system/user messages, templates | ✅ |
| — | RAG / tools / memory | (explore later) | 💡 LATER |

---

## You've Reached the End of the Lab

Sections 1–8 plus the microservices project cover the full backend roadmap. Loop back to the root `SpringBoot RoadMap.md` to check off what you've mastered.
