---
title: saas-olv — How Deployment Works (Beginner Friendly)
description: A simple, plain-language explanation of how the saas-olv project is built and deployed to Kubernetes via Jenkins + Kaniko on NCP gov cloud
category: egov-sample
tags: [egov-sample, deployment, ci-cd, jenkins, kubernetes, docker, kaniko]
---

# 🚀 saas-olv — How Deployment Works (the simple version)

> I read the real deploy files (`Jenkinsfile`, the 3 `Dockerfile-*`, `Jenkins-k8s-*.yaml`)
> and wrote this down in plain language. Goal: understand the big picture first,
> details later. (Read-only — nothing in saas-olv was changed.)

---

## 1. The one-sentence summary

> When you click "build" in Jenkins, it **compiles the code into WAR files**, **packs
> each WAR into a Docker image**, **pushes the image to a private registry**, and then
> **tells Kubernetes to run the new image** — all automatically.

That's it. Everything below is just the details of those four steps.

---

## 2. The words you need (glossary)

| Word | Think of it as… |
|------|-----------------|
| **WAR file** | a zip of the compiled Java web app (`adlunch.war`, `lunch.war`, `apilunch.war`) |
| **Docker image** | a box that contains the app + everything it needs to run (Tomcat, the WAR, libs) |
| **Registry** | a warehouse where Docker images are stored (here: NCP's private registry) |
| **Kubernetes (K8s)** | the system that runs the images as live servers and keeps them healthy |
| **Jenkins** | the robot that does build → image → deploy for you (CI/CD) |
| **Kaniko** | a tool that builds Docker images *inside* Kubernetes (no Docker needed) |
| **StatefulSet** | the K8s object that actually runs your app containers |
| **contextPath** | the URL prefix an app lives under (`/lunch`, `/adlunch`, `/apilunch`) |

---

## 3. Two environments, decided by branch

The same pipeline serves two worlds — it just looks at which git branch you build:

| Git branch | Environment | Names start with | Registry |
|------------|-------------|------------------|----------|
| `main` | **Production** (real users) | `prd-olv-…` | `avobxiry.private-ncr…` |
| `develop` (or others) | **Development** (testing) | `dev-olv-…` | `pqf1vv9m.private-ncr…` |

So building `main` = deploy to production. Building `develop` = deploy to dev. Same steps, different targets.

---

## 4. The deploy flow (picture)

```
   You (Jenkins UI)
   tick boxes: API? WEB? WAS_PFOM? WAS_OPER?
        │
        ▼
 ┌──────────────────────────────────────────────────────────┐
 │  JENKINS PIPELINE  (runs inside a temporary K8s pod)       │
 │                                                            │
 │  1. validate   → at least one box ticked?                  │
 │  2. warm up    → pre-download base images (faster build)   │
 │  3. pull config→ get application.yml from a SEPARATE repo   │
 │  4. build      → gradle builds the WAR files               │
 │  5. image      → Kaniko packs each WAR into a Docker image │
 │                  and pushes it to the registry             │
 │  6. deploy     → kubectl tells K8s to use the new image    │
 └──────────────────────────────────────────────────────────┘
        │
        ▼
   KUBERNETES runs the new containers (rolling update)
        │
        ▼
   Users hit the running app  (/lunch, /adlunch, /apilunch)
```

You pick **which parts** to deploy with checkboxes, so you can ship just the admin
app, or just the API, without rebuilding everything.

---

## 5. What each pipeline step does (a little more detail)

1. **validate params** — makes sure you ticked at least one box. Creates a unique
   version tag = `branch + short git commit` (e.g. `develop-a1b2c3d`). This tag is
   *immutable* — every build gets its own, so you always know exactly what's running.

2. **warm up** — pre-pulls the "base images" into a cache so the real build is fast.

3. **pull config** 🌟 *(important idea)* — the `application.yml` (DB passwords, URLs,
   env settings) is **NOT** stored in the code repo. It lives in a **separate
   `…-config` repo**, one branch per environment. Jenkins clones it and copies the
   right `application.yml` in before building.
   → *Why:* keeps secrets out of the source code, and lets dev/prod differ safely.
   This is the "externalized configuration" / 12-factor idea.

4. **build** — runs `gradle clean :module:build` for the chosen modules, producing:
   - `olv-api`  → `apilunch.war`
   - `olv-pfom` → `lunch.war`   (user portal)
   - `olv-oper` → `adlunch.war` (admin)

5. **image (Kaniko)** — builds Docker images *in the cluster* (no Docker daemon).
   Each image is tagged twice: the unique `branch-sha` **and** `latest`.
   Two kinds of image:
   - **WAS image** (`Dockerfile-was`): Tomcat + the WAR + the KeyBiz e-signature SDK.
     Runs as a **non-root** user (security).
   - **WEB image** (`Dockerfile-web-*`): just the **static files** (css/js/images),
     served by a web server (httpd/nginx) separately from the app.

6. **deploy** — `kubectl set image statefulset/… = newImage` swaps in the new image.
   K8s does a **rolling update** (replaces pods gradually, no downtime).

---

## 6. How it runs after deploy (the topology)

Two "tiers", each its own K8s namespace:

```
        Internet
           │
           ▼
   ┌───────────────┐   static files (css/js/img)
   │  WEB tier      │── served fast by httpd/nginx
   │ (…-olv-web ns) │   if a file is missing → ask the WAS
   └───────┬────────┘
           │ dynamic pages / API
           ▼
   ┌───────────────────────────────┐
   │  WAS tier (…-olv-was namespace)│   Tomcat servers running the WARs:
   │   • …-olv-pfom  → /lunch       │   (user portal)
   │   • …-olv-oper  → /adlunch     │   (admin)
   │   • …-olv-api   → /apilunch    │   (REST API)
   └───────────────────────────────┘
```

**Why the URL prefixes?** The prefix comes straight from the WAR file name
(`lunch.war` → `/lunch`). That's why the project rule says *never hardcode `/...`
paths* in templates — always use `@{/...}` so the prefix is added automatically.

---

## 7. The 6 ideas worth remembering

1. **Branch decides environment** — `main` = prod, `develop` = dev.
2. **Config is separate from code** — pulled from a `-config` repo at build time.
3. **WAR → Docker image → registry → K8s** is the whole journey.
4. **Immutable tags** (`branch-sha`) — you can always tell exactly what's deployed.
5. **Static and dynamic are split** — web tier (files) vs WAS tier (Tomcat apps).
6. **Rolling updates** — `kubectl set image` swaps versions with no downtime.

---

## 8. If I want to learn this hands-on (later)

I can't reproduce the gov-cloud setup, but the *concepts* are practiceable:
- **Docker basics** — write a `Dockerfile` for one of my `spring-boot-lab` apps,
  `docker build`, `docker run`. (Closest first step.)
- **A tiny Jenkinsfile** — 3 stages: build → image → echo "deploy".
- **Kubernetes locally** — run Minikube/kind, `kubectl apply` a Deployment + Service,
  then `kubectl set image` to see a rolling update.
- Map each thing back to this note so the saas-olv pipeline stops looking scary.

> Companion notes: [tech stack & concepts](./tech-stack-and-concepts.md) ·
> [screen-build flow](./README.md) · [OOP in saas-olv](./oop-in-saas-olv.md)
