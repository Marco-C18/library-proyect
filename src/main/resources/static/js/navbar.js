document.addEventListener("DOMContentLoaded", function () {
  const userMenuToggle = document.getElementById("userMenuToggle");
  const userDropdown = document.getElementById("userDropdown");
  const dropdownArrow = document.querySelector(".dropdown-arrow");

  userMenuToggle.addEventListener("click", function (e) {
    e.stopPropagation();
    userDropdown.classList.toggle("active");
    dropdownArrow.classList.toggle("rotated");
  });

  document.addEventListener("click", function (e) {
    if (
      !userMenuToggle.contains(e.target) &&
      !userDropdown.contains(e.target)
    ) {
      userDropdown.classList.remove("active");
      dropdownArrow.classList.remove("rotated");
    }
  });

  userDropdown.addEventListener("click", function (e) {
    e.stopPropagation();
  });
});
