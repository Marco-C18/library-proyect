const togglePassword = document.getElementById("togglePassword");
const passwordInput = document.getElementById("contrasena");

togglePassword.addEventListener("click", function () {
  const type =
    passwordInput.getAttribute("type") === "password" ? "text" : "password";
  passwordInput.setAttribute("type", type);
  this.classList.toggle("fa-eye");
  this.classList.toggle("fa-eye-slash");
});

document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();

  document
    .querySelectorAll(".error-message")
    .forEach((el) => (el.textContent = ""));
  document
    .querySelectorAll(".input-icon input")
    .forEach((el) => el.classList.remove("error"));

  let valid = true;

  const correo = document.getElementById("correo");
  const correoValue = correo.value.trim();
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!correoValue) {
    showError("correo", "El correo es obligatorio");
    valid = false;
  } else if (!emailRegex.test(correoValue)) {
    showError("correo", "Ingresa un correo válido");
    valid = false;
  }

  const contrasena = document.getElementById("contrasena");
  const contrasenaValue = contrasena.value;

  if (!contrasenaValue) {
    showError("contrasena", "La contraseña es obligatoria");
    valid = false;
  } else if (contrasenaValue.length < 6) {
    showError("contrasena", "La contraseña debe tener al menos 6 caracteres");
    valid = false;
  }

  if (valid) {
    this.submit();
  }
});

function showError(fieldId, message) {
  const errorSpan = document.getElementById(fieldId + "Error");
  const input = document.getElementById(fieldId);
  errorSpan.textContent = message;
  input.classList.add("error");
}

document.querySelectorAll(".input-icon input").forEach((input) => {
  input.addEventListener("input", function () {
    this.classList.remove("error");
    const errorSpan = document.getElementById(this.id + "Error");
    if (errorSpan) errorSpan.textContent = "";
  });
});
