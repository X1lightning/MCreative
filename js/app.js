/*
  M Creatives Art Deco site interactions.
  Run locally: open index.html in a browser. Ensure ./assets/M.svg exists.
*/
const serviceDetails = {
  graphic: {
    title: "Graphic Design",
    body: "We build bold identity systems that balance geometry, contrast, and elegance.",
    list: ["Brand identity and typography", "Packaging and editorial layouts", "Campaign art direction"]
  },
  engraving: {
    title: "Laser Engraving",
    body: "Precision engraving for premium materials that feel tactile and luxurious.",
    list: ["Brass and acrylic signage", "Custom gifting programs", "Small-batch product tags"]
  },
  marketing: {
    title: "Marketing",
    body: "Strategic campaigns with a luxe aesthetic, tailored to modern audiences.",
    list: ["Launch campaigns", "Social and content systems", "Retail + event activations"]
  }
};

const portfolioItems = [
  {
    title: "Aurum Hotel",
    tag: "Hospitality",
    body: "A gold-forward identity and wayfinding suite for a boutique hotel brand.",
    pattern: "pattern-1"
  },
  {
    title: "Velvet Line",
    tag: "Fashion",
    body: "Deco-inspired packaging and lookbook layouts for a luxury label.",
    pattern: "pattern-2"
  },
  {
    title: "Orion Devices",
    tag: "Tech",
    body: "A geometric brand system and launch kit for a premium electronics debut.",
    pattern: "pattern-3"
  },
  {
    title: "Deco Apothecary",
    tag: "Retail",
    body: "Custom label system and engraved fixtures for a modern apothecary.",
    pattern: "pattern-4"
  },
  {
    title: "Midnight Gala",
    tag: "Events",
    body: "Invitation suite, stage graphics, and signage for a black-tie event.",
    pattern: "pattern-5"
  },
  {
    title: "Brass & Bloom",
    tag: "Culinary",
    body: "Menu design and engraved table details for a chef's tasting room.",
    pattern: "pattern-6"
  },
  {
    title: "Atlas Atelier",
    tag: "Luxury",
    body: "Identity and custom monograms for a bespoke tailoring house.",
    pattern: "pattern-7"
  },
  {
    title: "Marble Glow",
    tag: "Wellness",
    body: "Branding and packaging for a modern spa and skincare line.",
    pattern: "pattern-8"
  }
];

const focusableSelectors = [
  "a[href]",
  "button:not([disabled])",
  "textarea:not([disabled])",
  "input:not([disabled])",
  "select:not([disabled])",
  "[tabindex]:not([tabindex='-1'])"
].join(",");

let activeModal = null;
let lastFocusedElement = null;
let portfolioIndex = 0;

const serviceModal = document.getElementById("service-modal");
const portfolioModal = document.getElementById("portfolio-modal");
const toast = document.getElementById("toast");

const setGlint = (element) => {
  element.classList.remove("glint-active");
  void element.offsetWidth;
  element.classList.add("glint-active");
};

const openModal = (modal) => {
  lastFocusedElement = document.activeElement;
  modal.hidden = false;
  activeModal = modal;
  const focusable = modal.querySelectorAll(focusableSelectors);
  if (focusable.length) {
    focusable[0].focus();
  }
  document.body.style.overflow = "hidden";
};

const closeModal = () => {
  if (!activeModal) return;
  activeModal.hidden = true;
  document.body.style.overflow = "";
  if (lastFocusedElement) {
    lastFocusedElement.focus();
  }
  activeModal = null;
};

const handleTrapFocus = (event) => {
  if (!activeModal || event.key !== "Tab") return;
  const focusable = Array.from(activeModal.querySelectorAll(focusableSelectors));
  if (!focusable.length) return;
  const first = focusable[0];
  const last = focusable[focusable.length - 1];
  if (event.shiftKey && document.activeElement === first) {
    event.preventDefault();
    last.focus();
  } else if (!event.shiftKey && document.activeElement === last) {
    event.preventDefault();
    first.focus();
  }
};

const renderServiceModal = (key) => {
  const data = serviceDetails[key];
  if (!data) return;
  document.getElementById("service-title").textContent = data.title;
  document.getElementById("service-body").textContent = data.body;
  const list = document.getElementById("service-list");
  list.innerHTML = "";
  data.list.forEach((item) => {
    const li = document.createElement("li");
    li.textContent = item;
    list.appendChild(li);
  });
};

const renderPortfolioModal = (index) => {
  const item = portfolioItems[index];
  if (!item) return;
  portfolioIndex = index;
  const preview = document.getElementById("portfolio-preview");
  preview.className = `portfolio-preview ${item.pattern}`;
  document.getElementById("portfolio-tag").textContent = item.tag;
  document.getElementById("portfolio-title").textContent = item.title;
  document.getElementById("portfolio-body").textContent = item.body;
};

const showToast = () => {
  toast.hidden = false;
  setTimeout(() => {
    toast.hidden = true;
  }, 2600);
};

const scrollToContact = () => {
  const contact = document.getElementById("contact");
  if (activeModal) {
    closeModal();
  }
  contact.scrollIntoView({ behavior: "smooth" });
  const input = contact.querySelector("input");
  if (input) {
    setTimeout(() => input.focus(), 500);
  }
};

const clearErrors = (form) => {
  form.querySelectorAll(".error-text").forEach((el) => el.remove());
  form.querySelectorAll(".input-error").forEach((el) => el.classList.remove("input-error"));
};

const setError = (input, message) => {
  const error = document.createElement("span");
  error.className = "error-text";
  error.textContent = message;
  input.classList.add("input-error");
  input.closest("label").appendChild(error);
};

const validateForm = (form) => {
  clearErrors(form);
  let valid = true;
  const name = form.elements.name;
  const email = form.elements.email;
  const message = form.elements.message;

  if (!name.value.trim()) {
    setError(name, "Please enter your name.");
    valid = false;
  }

  if (!email.value.trim() || !email.value.includes("@")) {
    setError(email, "Please enter a valid email.");
    valid = false;
  }

  if (!message.value.trim()) {
    setError(message, "Please add a brief message.");
    valid = false;
  }

  return valid;
};

const initNavHighlight = () => {
  const links = document.querySelectorAll(".nav-link");
  const sections = Array.from(links).map((link) => document.querySelector(link.getAttribute("href")));
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          links.forEach((link) => link.classList.remove("active"));
          const activeLink = document.querySelector(`.nav-link[href='#${entry.target.id}']`);
          if (activeLink) activeLink.classList.add("active");
        }
      });
    },
    { rootMargin: "-40% 0px -55% 0px" }
  );
  sections.forEach((section) => section && observer.observe(section));
};

const bindModalClose = (modal) => {
  modal.addEventListener("click", (event) => {
    if (event.target.matches("[data-modal-close='true']")) {
      closeModal();
    }
  });
  modal.querySelector(".modal-close").addEventListener("click", closeModal);
};

const init = () => {
  document.querySelectorAll("[data-glint='true']").forEach((button) => {
    button.addEventListener("click", () => setGlint(button));
  });

  document.querySelectorAll("[data-action='quote']").forEach((button) => {
    button.addEventListener("click", scrollToContact);
  });

  document.querySelectorAll(".service-card").forEach((card) => {
    card.addEventListener("click", () => {
      const key = card.dataset.service;
      renderServiceModal(key);
      openModal(serviceModal);
    });
  });

  document.querySelectorAll(".portfolio-tile").forEach((tile) => {
    tile.addEventListener("click", () => {
      const index = Number(tile.dataset.portfolio);
      renderPortfolioModal(index);
      openModal(portfolioModal);
    });
  });

  document.getElementById("portfolio-prev").addEventListener("click", () => {
    const nextIndex = (portfolioIndex - 1 + portfolioItems.length) % portfolioItems.length;
    renderPortfolioModal(nextIndex);
  });

  document.getElementById("portfolio-next").addEventListener("click", () => {
    const nextIndex = (portfolioIndex + 1) % portfolioItems.length;
    renderPortfolioModal(nextIndex);
  });

  bindModalClose(serviceModal);
  bindModalClose(portfolioModal);

  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
      closeModal();
    }
    handleTrapFocus(event);
  });

  const form = document.getElementById("contact-form");
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    if (validateForm(form)) {
      form.reset();
      showToast();
    }
  });

  initNavHighlight();
};

document.addEventListener("DOMContentLoaded", init);
