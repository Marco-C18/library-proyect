document.addEventListener("DOMContentLoaded", function () {
  const bell = document.getElementById("notificationBell");
  const dropdown = document.getElementById("notificationDropdown");
  const list = document.getElementById("notificationList");
  const badge = document.querySelector(".notification-badge");

  // Mostrar u ocultar notificaciones
  bell.addEventListener("click", () => {
    dropdown.classList.toggle("show");
    if (dropdown.classList.contains("show")) {
      generarNotificaciones();
    }
  });

  // Cerrar si haces clic fuera del panel
  document.addEventListener("click", (e) => {
    if (!bell.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.classList.remove("show");
    }
  });

  function generarNotificaciones() {
    list.innerHTML = ""; 
    let contador = 0;

    // MORAS / DEVOLUCIONES PENDIENTES
    const librosMora = Array.from(document.querySelectorAll(".history-card"))
      .filter(card => card.textContent.includes("Devolver") || card.textContent.includes("Pendiente"));

    librosMora.forEach(card => {
      const titulo = card.querySelector(".history-book-title")?.textContent || "Libro sin título";
      const fecha = card.querySelector(".history-date")?.textContent || "Fecha no disponible";
      list.innerHTML += `
        <div class="notification-item">
          <div class="notification-icon-wrapper text-danger"><i class="fas fa-exclamation-triangle"></i></div>
          <div class="notification-texts">
            <p><strong>${titulo}</strong> debe ser devuelto pronto.</p>
            <small>Fecha límite: ${fecha}</small>
          </div>
        </div>
      `;
      contador++;
    });

    // PRÉSTAMOS ACTIVOS
    const prestamos = Array.from(document.querySelectorAll(".loan-card"));
    prestamos.forEach(card => {
      const titulo = card.querySelector(".loan-book-title")?.textContent || "Libro desconocido";
      const fechaDev = card.querySelector(".loan-return-date")?.textContent || "Sin fecha";
      list.innerHTML += `
        <div class="notification-item">
          <div class="notification-icon-wrapper text-primary"><i class="fas fa-book"></i></div>
          <div class="notification-texts">
            <p>Préstamo activo: <strong>${titulo}</strong></p>
            <small>Devolución: ${fechaDev}</small>
          </div>
        </div>
      `;
      contador++;
    });

    // 🔸 LIBROS DEVUELTOS
    const devueltos = Array.from(document.querySelectorAll(".history-card"))
      .filter(card => card.textContent.includes("Devuelto"));
    devueltos.forEach(card => {
      const titulo = card.querySelector(".history-book-title")?.textContent || "Libro";
      const fecha = card.querySelector(".history-date")?.textContent || "Sin fecha";
      list.innerHTML += `
        <div class="notification-item">
          <div class="notification-icon-wrapper text-success"><i class="fas fa-undo"></i></div>
          <div class="notification-texts">
            <p><strong>${titulo}</strong> ha sido devuelto correctamente.</p>
            <small>Fecha: ${fecha}</small>
          </div>
        </div>
      `;
      contador++;
    });

   
    if (contador === 0) {
      list.innerHTML = `<p class="text-center text-muted py-3 mb-0">No hay notificaciones disponibles.</p>`;
    }

    
    badge.style.display = contador > 0 ? "block" : "none";
  }
});
