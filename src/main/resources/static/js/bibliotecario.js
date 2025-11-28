document.addEventListener("DOMContentLoaded", function () {

  /* ---------- AJAX navigation for sidebar (single-page feel) ---------- */
  (function() {
    const main = document.getElementById('main-content');
    const sidebarLinks = document.querySelectorAll('.sidebar-menu .nav-link');

    function ajaxLoad(url, push = true) {
      fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => {
          if (!r.ok) throw new Error('Network error');
          return r.text();
        })
        .then(html => {
          const parser = new DOMParser();
          const doc = parser.parseFromString(html, 'text/html');
          const newMain = doc.querySelector('.main-content');
          if (newMain) {
            main.innerHTML = newMain.innerHTML;
            runAfterLoad(); // rebind buttons, modals, etc
            if (push) history.pushState({ url }, '', url);
          } else {
            location.href = url;
          }
        })
        .catch(err => {
          console.error('AJAX load failed:', err);
          location.href = url;
        });
    }

    sidebarLinks.forEach(a => {
      a.addEventListener('click', function(e) {
        if (e.ctrlKey || e.metaKey) return;
        e.preventDefault();
        const href = a.getAttribute('href');
        ajaxLoad(href, true);

        document.querySelectorAll('.sidebar-menu .menu-item')
          .forEach(el => el.classList.remove('active'));
        a.classList.add('active');
      });
    });

    window.addEventListener('popstate', function(ev) {
      if (ev.state && ev.state.url) {
        ajaxLoad(ev.state.url, false);
      } else {
        location.reload();
      }
    });

    function runAfterLoad() {
      const btnNew = document.getElementById('btn-new-book');
      if (btnNew) {
        btnNew.addEventListener('click', function() {
          openBookModal();
        });
      }

      document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', function() {
          const id = btn.getAttribute('data-id');
          if (!id) return;

          fetch('/bibliotecario/dashboard/libros/json/' + id)
            .then(r => r.json())
            .then(data => {
              if (!data.ok) { alert('No encontrado'); return; }
              openBookModal(data);
            })
            .catch(e => {
              console.error(e);
              alert('Error al cargar libro');
            });
        });
      });

      const bookForm = document.getElementById('bookForm');
      if (bookForm) {
        bookForm.addEventListener('submit', function() {
          const id = document.getElementById('book-id').value;
          if (id && id.trim() !== '') {
            bookForm.action = '/bibliotecario/dashboard/libros/editar/' + id;
          } else {
            bookForm.action = '/bibliotecario/dashboard/libros/guardar';
          }
        });
      }
    }

    function openBookModal(data) {
      const modalEl = document.getElementById('bookModal');
      const modal = new bootstrap.Modal(modalEl, { backdrop: 'static' });

      const idF = document.getElementById('book-id');
      const tF = document.getElementById('book-titulo');
      const aF = document.getElementById('book-autor');
      const yF = document.getElementById('book-anio');
      const cF = document.getElementById('book-categoria');
      const dF = document.getElementById('book-desc');
      const title = document.getElementById('bookModalTitle');

      idF.value = '';
      tF.value = '';
      aF.value = '';
      yF.value = '';
      if (cF) cF.value = '';
      dF.value = '';

      if (data && data.ok) {
        title.textContent = 'Editar libro';
        idF.value = data.idLibro || '';
        tF.value = data.titulo || '';
        aF.value = data.autor || '';
        yF.value = data.anio || '';
        dF.value = data.descripcion || '';

        if (data.categoria && cF) {
          for (let i = 0; i < cF.options.length; i++) {
            const opt = cF.options[i];
            if (opt.value === data.categoria || opt.textContent === data.categoriaDisplay) {
              cF.selectedIndex = i;
              break;
            }
          }
        }
      } else {
        title.textContent = 'Registrar libro';
      }

      modal.show();
    }

    runAfterLoad();

  })();

});
