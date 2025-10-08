
// Inicializar funcionalidad del Navbar
function initializeNavbar() {
    const hamburger = document.getElementById('hamburger');
    const navLinks = document.getElementById('navLinks');

    if (hamburger && navLinks) {
        // Evento click en el hamburger
        hamburger.addEventListener('click', (e) => {
            e.stopPropagation();
            hamburger.classList.toggle('active');
            navLinks.classList.toggle('active');
            console.log('Hamburger clicked'); // Para debug
        });

        // Cerrar menú al hacer clic en un enlace
        const links = navLinks.querySelectorAll('a');
        links.forEach(link => {
            link.addEventListener('click', (e) => {
                navLinks.classList.remove('active');
                hamburger.classList.remove('active');
            });
        });

        // Cerrar menú al hacer clic fuera
        document.addEventListener('click', (e) => {
            if (!hamburger.contains(e.target) && !navLinks.contains(e.target)) {
                navLinks.classList.remove('active');
                hamburger.classList.remove('active');
            }
        });
    } else {
        console.error('No se encontró hamburger o navLinks');
    }
}

// Smooth scroll para enlaces internos
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

// Intersection Observer para animaciones al scroll
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.animationPlayState = 'running';
            observer.unobserve(entry.target);
        }
    });
}, observerOptions);

// Observar elementos cuando el DOM esté listo
window.addEventListener('load', () => {
    const animatedElements = document.querySelectorAll(
        '.category-item, .book-card, .step, .testimonial-card'
    );
    
    animatedElements.forEach(el => {
        observer.observe(el);
    });
});

// Animación para el botón CTA
document.addEventListener('DOMContentLoaded', () => {
    const ctaButton = document.querySelector('.cta-button');
    if (ctaButton) {
        ctaButton.addEventListener('click', () => {
            // Aquí puedes agregar la lógica para redirigir al catálogo
            window.location.href = 'catalogo.html';
        });
    }
});

// Funcionalidad para los botones "Ver más" de los libros
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('book-button')) {
        // Aquí puedes agregar la lógica para ver detalles del libro
        console.log('Ver detalles del libro');
    }
});

// Animación del navbar al hacer scroll
let lastScroll = 0;

// Función para manejar el scroll del navbar
function handleNavbarScroll() {
    const navbar = document.querySelector('nav');
    if (!navbar) return;

    const currentScroll = window.pageYOffset;
    
    if (currentScroll <= 0) {
        navbar.style.boxShadow = '0 2px 10px rgba(0,0,0,0.1)';
        return;
    }
    
    if (currentScroll > lastScroll && currentScroll > 100) {
        navbar.style.transform = 'translateY(-100%)';
    } else {
        navbar.style.transform = 'translateY(0)';
        navbar.style.boxShadow = '0 2px 20px rgba(0,0,0,0.15)';
    }
    
    lastScroll = currentScroll;
}

window.addEventListener('scroll', handleNavbarScroll);

// Agregar estilos para la transición del navbar cuando cargue
setTimeout(() => {
    const navbar = document.querySelector('nav');
    if (navbar) {
        navbar.style.transition = 'transform 0.3s ease, box-shadow 0.3s ease';
    }
}, 100);