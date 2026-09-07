const categories = {
  medical: {
    title: "Medical",
    icon: "fa-heart-pulse",
    page: "donate-medical.html",
    text: "Support treatment, surgery, medicines, and hospital care."
  },
  education: {
    title: "Education",
    icon: "fa-graduation-cap",
    page: "donate-education.html",
    text: "Help students cover fees, books, uniforms, and training."
  },
  nonprofit: {
    title: "Non-profit",
    icon: "fa-hand-holding-heart",
    page: "donate-nonprofit.html",
    text: "Back trusted social causes, community work, and relief drives."
  },
  animal: {
    title: "Animal",
    icon: "fa-paw",
    page: "donate-animal.html",
    text: "Fund rescue, shelter, food, and urgent care for animals."
  },
  emergency: {
    title: "Emergency",
    icon: "fa-triangle-exclamation",
    page: "donate-emergency.html",
    text: "Give fast support during accidents, disasters, and family crises."
  }
};

// Static campaign lists removed. Campaign data is served from the backend APIs only.

function setupMobileMenu() {
  const menuBtn = document.querySelector(".menu-btn");
  const mobileMenu = document.querySelector("#mobile-menu");
  const mobileOverlay = document.querySelector("#mobile-overlay");
  if (!menuBtn || !mobileMenu || !mobileOverlay) return;

  function closeMobileMenu() {
    menuBtn.classList.remove("active");
    menuBtn.setAttribute("aria-expanded", "false");
    menuBtn.setAttribute("aria-label", "Open menu");
    mobileMenu.setAttribute("aria-hidden", "true");
    mobileMenu.classList.remove("show");
    mobileOverlay.setAttribute("aria-hidden", "true");
    mobileOverlay.classList.remove("show");
    document.body.classList.remove("menu-open");
  }

  function openMobileMenu() {
    menuBtn.classList.add("active");
    menuBtn.setAttribute("aria-expanded", "true");
    menuBtn.setAttribute("aria-label", "Close menu");
    mobileMenu.setAttribute("aria-hidden", "false");
    mobileMenu.classList.add("show");
    mobileOverlay.setAttribute("aria-hidden", "false");
    mobileOverlay.classList.add("show");
    document.body.classList.add("menu-open");
  }

  menuBtn.addEventListener("click", () => {
    mobileMenu.classList.contains("show") ? closeMobileMenu() : openMobileMenu();
  });

  mobileMenu.querySelectorAll("a").forEach(link => link.addEventListener("click", closeMobileMenu));
  mobileOverlay.addEventListener("click", closeMobileMenu);
  document.addEventListener("keydown", event => {
    if (event.key === "Escape" && mobileMenu.classList.contains("show")) closeMobileMenu();
  });
  window.addEventListener("resize", () => {
    if (window.innerWidth > 1050) closeMobileMenu();
  });
}


// local helper previously used static campaign arrays removed; use backendCampaignCard() instead

async function renderDonateSections() {
  const host = document.querySelector('[data-donate-sections]');
  if (!host) return;

  try {
    const resp = await fetch('/api/campaigns');
    if (!resp.ok) return;
    const data = await resp.json();
    const all = data.campaigns || [];

    host.innerHTML = Object.keys(categories).map(key => {
      const category = categories[key];
      const items = all.filter(c => (c.category || '').toLowerCase() === key || (key === 'nonprofit' && (c.category || '').toLowerCase() === 'community')).slice(0,6);
      const cards = items.map(backendCampaignCard).join('') || '<div class="empty-note">No fundraisers yet.</div>';
      return `
        <section class="category-section" id="${key}">
          <div class="section-inner">
            <div class="section-head">
              <div>
                <span class="kicker">${category.title}</span>
                <h2>${category.title} fundraisers</h2>
                <p>${category.text}</p>
              </div>
              <div class="slider-controls" aria-label="${category.title} carousel controls">
                <button type="button" data-scroll-row="${key}" data-direction="-1" aria-label="Previous ${category.title} cards"><i class="fa-solid fa-arrow-left"></i></button>
                <button type="button" data-scroll-row="${key}" data-direction="1" aria-label="Next ${category.title} cards"><i class="fa-solid fa-arrow-right"></i></button>
              </div>
            </div>
            <div class="campaign-row" data-row="${key}">${cards}</div>
            <div class="see-more-wrap"><a class="see-more" href="${category.page}" target="_blank" rel="noopener">See more</a></div>
          </div>
        </section>
      `;
    }).join('');
  } catch (err) { console.warn('Could not load campaigns for sections', err); }
}

async function renderCategoryGrid() {
  const grid = document.querySelector('[data-category-grid]');
  if (!grid) return;

  const categoryKey = grid.dataset.categoryGrid;
  const title = document.querySelector('[data-category-title]');
  const desc = document.querySelector('[data-category-desc]');
  const category = categories[categoryKey];

  if (title) title.textContent = `${category.title} fundraisers`;
  if (desc) desc.textContent = category.text;

  try {
    const resp = await fetch('/api/campaigns');
    if (!resp.ok) return;
    const data = await resp.json();
    const all = data.campaigns || [];
    const matching = all.filter(c => (c.category || '').toLowerCase() === categoryKey || (categoryKey === 'nonprofit' && (c.category || '').toLowerCase() === 'community'));
    grid.innerHTML = matching.length ? matching.map(backendCampaignCard).join('') : '<p>No campaigns found.</p>';
  } catch (err) { console.warn('Could not load category grid campaigns', err); }
}

function setupRowButtons() {
  document.querySelectorAll("[data-scroll-row]").forEach(button => {
    button.addEventListener("click", () => {
      const row = document.querySelector(`[data-row="${button.dataset.scrollRow}"]`);
      if (!row) return;
      const direction = Number(button.dataset.direction || 1);
      row.scrollBy({ left: direction * row.clientWidth * 0.85, behavior: "smooth" });
    });
  });
}

function renderFundraiseCategories() {
  const grid = document.querySelector("[data-fundraise-categories]");
  if (!grid) return;

  grid.innerHTML = Object.keys(categories).map(key => {
    const category = categories[key];
    return `
      <article class="category-tile">
        <div class="category-icon"><i class="fa-solid ${category.icon}"></i></div>
        <h3>${category.title}</h3>
        <p>${category.text}</p>
        <a class="category-action" href="start-fundraiser.html">Start now</a>
      </article>
    `;
  }).join("");
}

setupMobileMenu();
renderDonateSections();
renderCategoryGrid();
renderFundraiseCategories();
setupRowButtons();

// click handler that relied on static campaign arrays removed — live cards include links/buttons
const kindrStyle=document.createElement("link");kindrStyle.rel="stylesheet";kindrStyle.href="app.css";document.head.append(kindrStyle);if(!window.Kindr){const kindrScript=document.createElement("script");kindrScript.src="app.js";document.body.append(kindrScript)}



function backendCampaignCard(campaign) {
  const pct = Math.min(100, Math.round((Number(campaign.raised) / Number(campaign.goal)) * 100) || 0);
  const clean = value => String(value || "").replace(/[&<>"']/g, char => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[char]));
  return `<article class="campaign-card live-campaign-card">
    <img class="campaign-art campaign-photo" src="/api/campaigns/${encodeURIComponent(campaign.id)}/image" onerror="this.onerror=null;this.src='hero1.jpg'" alt="${clean(campaign.title)}">
    <div class="campaign-body"><span class="live-badge">New fundraiser</span><h3>${clean(campaign.title)}</h3><p>${clean(campaign.story).slice(0, 115)}${String(campaign.story || "").length > 115 ? "…" : ""}</p>
    <div class="progress" aria-label="${pct}% funded"><span style="width:${pct}%"></span></div><div class="campaign-meta"><span><b>₹${Number(campaign.raised).toLocaleString("en-IN")}</b> raised</span><span>₹${Number(campaign.goal).toLocaleString("en-IN")}</span></div>
    <a class="campaign-donate" href="campaign.html?id=${encodeURIComponent(campaign.id)}">View fundraiser</a></div></article>`;
}
async function renderBackendCampaigns() {
  try {
    const response = await fetch("/api/campaigns");
    if (!response.ok) return;
    const data = await response.json();
    const live = (data.campaigns || []).filter(c => c.ownerId !== "platform" && c.status === "Active");
    const host = document.querySelector("[data-donate-sections]");
    if (host && live.length) {
      const section = document.createElement("section"); section.className = "category-section live-section"; section.id = "new-fundraisers";
      section.innerHTML = `<div class="section-inner"><div class="section-head"><div><span class="kicker">Just launched</span><h2>New fundraisers from our community</h2><p>Support the latest campaigns created by Kindr members.</p></div></div><div class="campaign-row live-campaign-row">${live.map(backendCampaignCard).join("")}</div></div>`;
      host.prepend(section);
    }
    const grid = document.querySelector("[data-category-grid]");
    if (grid) {
      const key = grid.dataset.categoryGrid;
      const matching = live.filter(c => c.category.toLowerCase() === key || (key === "nonprofit" && c.category.toLowerCase() === "community"));
      if (matching.length) grid.insertAdjacentHTML("afterbegin", matching.map(backendCampaignCard).join(""));
    }
  } catch (error) { console.warn("Could not load live campaigns", error); }
}
renderBackendCampaigns();

// Contact form submission: collect form fields and POST JSON to backend
document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector('#contact-form');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
      firstName: (form.querySelector('#firstName') || {}).value || '',
      lastName: (form.querySelector('#lastName') || {}).value || '',
      email: (form.querySelector('#email') || {}).value || '',
      mobile: (form.querySelector('#mobile') || {}).value || '',
      topic: (form.querySelector('#topic') || {}).value || '',
      message: (form.querySelector('#message') || {}).value || ''
    };

    // Normalize mobile: strip non-digits and use last 10 digits (suits Indian mobiles)
    if (data.mobile) {
      const digits = data.mobile.replace(/\D/g, '');
      data.mobile = digits.length > 10 ? digits.slice(-10) : digits;
    }

    try {
      const res = await fetch('/api/contact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (res.ok) {
        alert('Message sent — thank you!');
        form.reset();
      } else {
        const text = await res.text();
        console.error('Contact POST failed', res.status, text);
        alert('Could not send message. Please try again later.');
      }
    } catch (err) {
      console.error('Network error posting contact', err);
      alert('Network error — please check your connection.');
    }
  });
});
