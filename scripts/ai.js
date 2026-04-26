const OpenAI = require("openai");
const simpleGit = require("simple-git");
const fs = require("fs");
const { execSync } = require("child_process");

const openai = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY,
});

const issueTitle = process.env.ISSUE_TITLE;
const issueBody = process.env.ISSUE_BODY;

const branchName = `ai/${issueTitle.toLowerCase().replace(/\s/g, "-")}`;

async function run() {
  // 1. AI prompt
  const response = await openai.chat.completions.create({
    model: "gpt-4o-mini",
    messages: [
      {
        role: "system",
        content: "You are a senior software engineer. Output ONLY code files."
      },
      {
        role: "user",
        content: `Create code for this issue:

TITLE: ${issueTitle}
DESCRIPTION: ${issueBody}`
      }
    ]
  });

  const code = response.choices[0].message.content;

  // 2. write file
  fs.writeFileSync("ai-output.txt", code);

  const git = simpleGit();

  // 3. create branch
  await git.checkoutLocalBranch(branchName);

  // 4. commit
  await git.add(".");
  await git.commit("AI generated code");

  // 5. push branch
  await git.push("origin", branchName);

  // 6. create PR via GitHub CLI
  execSync(`
    gh pr create \
    --title "AI: ${issueTitle}" \
    --body "Auto-generated PR from issue: ${issueBody}" \
    --head ${branchName} \
    --base main
  `);

  console.log("PR created!");
}

run();
