import fs from "fs";
import simpleGit from "simple-git";

const issueTitle = process.env.ISSUE_TITLE;
const issueBody = process.env.ISSUE_BODY;
const apiKey = process.env.GROQ_API_KEY;
const githubToken = process.env.GITHUB_TOKEN;
const repo = process.env.GITHUB_REPOSITORY;

const branchName = `ai/${issueTitle.toLowerCase().replace(/\s/g, "-")}`;

async function run() {
  // 🧠 1. AI PROMPT
  const prompt = `
You are a senior software engineer.

Create production-ready code for this GitHub issue:

TITLE: ${issueTitle}
DESCRIPTION:
${issueBody}

Return ONLY code.
`;

  // ⚡ 2. GROQ API CALL
  const response = await fetch("https://api.groq.com/openai/v1/chat/completions", {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${apiKey}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      model: "llama-3.1-70b-versatile",
      messages: [
        { role: "user", content: prompt }
      ],
      temperature: 0.2
    })
  });

  const data = await response.json();
  const code = data.choices[0].message.content;

  // 💾 3. Save output
  fs.writeFileSync("ai-output.txt", code);

  // 🌿 4. Git operations
  const git = simpleGit();

  await git.checkoutLocalBranch(branchName);
  await git.add(".");
  await git.commit("AI generated code (Groq)");
  await git.push("origin", branchName);

  console.log("Branch pushed:", branchName);

  // 🔥 5. PR CREATE (EKSİK OLAN KISIM BURASI)
  await fetch(`https://api.github.com/repos/${repo}/pulls`, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${githubToken}`,
      "Content-Type": "application/json",
      "Accept": "application/vnd.github+json"
    },
    body: JSON.stringify({
      title: `AI: ${issueTitle}`,
      head: branchName,
      base: "main",
      body: `🤖 Auto-generated PR from issue:\n\n${issueBody}`
    })
  });

  console.log("PR created!");
}

run();
