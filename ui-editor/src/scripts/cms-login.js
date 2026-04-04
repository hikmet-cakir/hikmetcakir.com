export function initPasswordToggle() {
  const eye = document.getElementById("eye");
  const pwd = document.getElementById("pwd");
  if (!eye || !pwd) return;

  let pwShown = false;

  eye.addEventListener("click", () => {
    if (!pwShown) {
      pwd.type = "text";
      eye.classList.remove("fa-eye-slash");
      eye.classList.add("fa-eye");
      pwShown = true;
    } else {
      pwd.type = "password"; 
      eye.classList.remove("fa-eye");
      eye.classList.add("fa-eye-slash");
      pwShown = false;
    }
  });
}