export function initPasswordToggle() {
  const eye = document.getElementById("eye");
  const pwd = document.getElementById("pwd");
  if (!eye || !pwd) return;

  let pwShown = false;

  eye.addEventListener("click", () => {
    if (!pwShown) {
      pwd.type = "text";
      pwShown = true;
    } else {
      pwd.type = "password";
      pwShown = false;
    }
  });
}