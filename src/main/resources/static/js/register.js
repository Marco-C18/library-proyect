// Toggle password visibility
function setupPasswordToggle(toggleId, inputId) {
    const toggle = document.getElementById(toggleId);
    const input = document.getElementById(inputId);

    toggle.addEventListener('click', function () {
        const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
        input.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
}

setupPasswordToggle('togglePassword1', 'contrasena');
setupPasswordToggle('togglePassword2', 'confirmarContrasena');

// Validar solo números en teléfono
document.getElementById('telefono').addEventListener('input', function (e) {
    this.value = this.value.replace(/[^0-9]/g, '');
});

// Validación del formulario
document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // Limpiar mensajes de error previos
    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
    document.querySelectorAll('.input-icon input').forEach(el => el.classList.remove('error'));

    let valid = true;

    // Validar nombre
    const nombre = document.getElementById('nombre');
    const nombreValue = nombre.value.trim();
    if (!nombreValue) {
        showError('nombre', 'El nombre es obligatorio');
        valid = false;
    } else if (nombreValue.length < 2) {
        showError('nombre', 'El nombre debe tener al menos 2 caracteres');
        valid = false;
    }

    // Validar apellido
    const apellido = document.getElementById('apellido');
    const apellidoValue = apellido.value.trim();
    if (!apellidoValue) {
        showError('apellido', 'El apellido es obligatorio');
        valid = false;
    } else if (apellidoValue.length < 2) {
        showError('apellido', 'El apellido debe tener al menos 2 caracteres');
        valid = false;
    }

    // Validar correo
    const correo = document.getElementById('correo');
    const correoValue = correo.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!correoValue) {
        showError('correo', 'El correo es obligatorio');
        valid = false;
    } else if (!emailRegex.test(correoValue)) {
        showError('correo', 'Ingresa un correo válido');
        valid = false;
    }

    // Validar teléfono
    const telefono = document.getElementById('telefono');
    const telefonoValue = telefono.value.trim();

    if (!telefonoValue) {
        showError('telefono', 'El teléfono es obligatorio');
        valid = false;
    } else if (telefonoValue.length < 9) {
        showError('telefono', 'El teléfono debe tener al menos 9 dígitos');
        valid = false;
    }

    // Validar contraseña
    const contrasena = document.getElementById('contrasena');
    const contrasenaValue = contrasena.value;

    if (!contrasenaValue) {
        showError('contrasena', 'La contraseña es obligatoria');
        valid = false;
    } else if (contrasenaValue.length < 6) {
        showError('contrasena', 'La contraseña debe tener al menos 6 caracteres');
        valid = false;
    }

    // Validar confirmar contraseña
    const confirmarContrasena = document.getElementById('confirmarContrasena');
    const confirmarValue = confirmarContrasena.value;

    if (!confirmarValue) {
        showError('confirmarContrasena', 'Debes confirmar la contraseña');
        valid = false;
    } else if (contrasenaValue !== confirmarValue) {
        showError('confirmarContrasena', 'Las contraseñas no coinciden');
        valid = false;
    }

    if (valid) {
        this.submit();
    }
});

function showError(fieldId, message) {
    const errorSpan = document.getElementById(fieldId + 'Error');
    const input = document.getElementById(fieldId);
    errorSpan.textContent = message;
    input.classList.add('error');
}

// Limpiar error al escribir
document.querySelectorAll('.input-icon input').forEach(input => {
    input.addEventListener('input', function () {
        this.classList.remove('error');
        const errorSpan = document.getElementById(this.id + 'Error');
        if (errorSpan) errorSpan.textContent = '';
    });
});