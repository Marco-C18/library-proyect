// Inicializar funcionalidad del Navbar
function initializeNavbar() {
  const hamburger = document.getElementById("hamburger");
  const navLinks = document.getElementById("navLinks");

  if (hamburger && navLinks) {
    hamburger.addEventListener("click", (e) => {
      e.stopPropagation();
      hamburger.classList.toggle("active");
      navLinks.classList.toggle("active");
      console.log("Hamburger clicked");
    });

    const links = navLinks.querySelectorAll("a");
    links.forEach((link) => {
      link.addEventListener("click", (e) => {
        navLinks.classList.remove("active");
        hamburger.classList.remove("active");
      });
    });

    document.addEventListener("click", (e) => {
      if (!hamburger.contains(e.target) && !navLinks.contains(e.target)) {
        navLinks.classList.remove("active");
        hamburger.classList.remove("active");
      }
    });
  } else {
    console.error("No se encontró hamburger o navLinks");
  }
}

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();
      const target = document.querySelector(this.getAttribute("href"));
      if (target) {
        target.scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }
    });
  });
});

const observerOptions = {
  threshold: 0.1,
  rootMargin: "0px 0px -100px 0px",
};

const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      entry.target.style.animationPlayState = "running";
      observer.unobserve(entry.target);
    }
  });
}, observerOptions);

window.addEventListener("load", () => {
  const animatedElements = document.querySelectorAll(
    ".category-item, .book-card, .step, .testimonial-card"
  );

  animatedElements.forEach((el) => {
    observer.observe(el);
  });
});

document.addEventListener("DOMContentLoaded", () => {
  const ctaButton = document.querySelector(".cta-button");
  if (ctaButton) {
    ctaButton.addEventListener("click", () => {
      window.location.href = "catalogo.html";
    });
  }
});

document.addEventListener("click", (e) => {
  if (e.target.classList.contains("book-button")) {
    console.log("Ver detalles del libro");
  }
});

let lastScroll = 0;

function handleNavbarScroll() {
  const navbar = document.querySelector("nav");
  if (!navbar) return;

  const currentScroll = window.pageYOffset;

  if (currentScroll <= 0) {
    navbar.style.boxShadow = "0 2px 10px rgba(0,0,0,0.1)";
    return;
  }

  if (currentScroll > lastScroll && currentScroll > 100) {
    navbar.style.transform = "translateY(-100%)";
  } else {
    navbar.style.transform = "translateY(0)";
    navbar.style.boxShadow = "0 2px 20px rgba(0,0,0,0.15)";
  }

  lastScroll = currentScroll;
}

window.addEventListener("scroll", handleNavbarScroll);

setTimeout(() => {
  const navbar = document.querySelector("nav");
  if (navbar) {
    navbar.style.transition = "transform 0.3s ease, box-shadow 0.3s ease";
  }
}, 100);
