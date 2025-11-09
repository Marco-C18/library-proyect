document.addEventListener('DOMContentLoaded', function () {
    const userMenuToggle = document.getElementById('userMenuToggle');
    const userDropdown = document.getElementById('userDropdown');
    const dropdownArrow = document.querySelector('.dropdown-arrow');

    // Toggle del menú de usuario
    userMenuToggle.addEventListener('click', function (e) {
        e.stopPropagation();
        userDropdown.classList.toggle('active');
        dropdownArrow.classList.toggle('rotated');
    });

    // Cerrar el dropdown al hacer clic fuera
    document.addEventListener('click', function (e) {
        if (!userMenuToggle.contains(e.target) && !userDropdown.contains(e.target)) {
            userDropdown.classList.remove('active');
            dropdownArrow.classList.remove('rotated');
        }
    });

    // Prevenir que el dropdown se cierre al hacer clic dentro
    userDropdown.addEventListener('click', function (e) {
        e.stopPropagation();
    });
});