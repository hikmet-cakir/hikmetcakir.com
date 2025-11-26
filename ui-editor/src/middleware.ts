import { defineMiddleware } from "astro/middleware";

export const onRequest = defineMiddleware(async ({ request, redirect }, next) => {
  const url = new URL(request.url);
  const path = url.pathname;

  const publicPaths = ["/login", "/favicon.ico"];

  const cookies = request.headers.get("cookie") || "";
  const token = cookies.split("; ").find(c => c.trim().startsWith("access_token="))?.split("=")[1];

  if (publicPaths.includes(path)) {
    if (path === "/login" && token) {
      const verifyRes = await fetch("http://localhost:8080/auth/verify", {
        method: "GET",
        headers: { Cookie: `access_token=${token}` },
        credentials: "include"
      });

      if (verifyRes.ok) {
        return redirect("/dashboard");
      }
    }
    return next();
  }

  if (!token) {
    return redirect("/login");
  }

  const verifyRes = await fetch("http://localhost:8080/auth/verify", {
    method: "GET",
    headers: { Cookie: `access_token=${token}` },
    credentials: "include"
  });

  if (!verifyRes.ok) {
    return redirect("/login");
  }

  return next();
});
