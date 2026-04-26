const fs = require("fs");
const simpleGit = require("simple-git");

const issueTitle = process.env.ISSUE_TITLE;
const issueBody = process.env.ISSUE_BODY;
const apiKey = process.env.GROQ_API_KEY;

const branchName = `ai/${issueTitle.toLowerCase().replace(/\s/g, "-")}`;

async function run() {
  const prompt = `
You are a senior software engineer.

Create production-ready code for this GitHub issue:

TITLE: ${issueTitle}
DESCRIPTION: ${issueBody}

Return ONLY code.
`;

  // 1. Groq API call
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

  fs.writeFileSync("ai-output.txt", code);

  // 2. Git operations
  const git = simpleGit();

  await git.checkoutLocalBranch(branchName);

  await git.add(".");
  await git.commit("AI generated code (Groq)");

  await git.push("origin", branchName);

  console.log("Branch pushed:", branchName);
}

run();

await fetch(`https://api.github.com/repos/${process.env.GITHUB_REPOSITORY}/pulls`, {
  method: "POST",
  headers: {
    "Authorization": `Bearer ${process.env.GITHUB_TOKEN}`,
    "Content-Type": "application/json"
  },
  body: JSON.stringify({
    title: `AI: ${issueTitle}`,
    head: branchName,
    base: "main",
    body: issueBody
  })
});
