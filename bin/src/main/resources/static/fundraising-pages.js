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

const campaigns = {
  medical: [
    ["Help Aarav complete heart surgery", "Critical pediatric care and medicines.", 68, "â‚¹3,40,000", "â‚¹5,00,000"],
    ["Cancer treatment support for Meera", "Chemotherapy, scans, and travel support.", 54, "â‚¹2,70,000", "â‚¹5,00,000"],
    ["Kidney transplant recovery fund", "Post-surgery care and hospital expenses.", 76, "â‚¹7,60,000", "â‚¹10,00,000"],
    ["Emergency ICU care for Rohan", "Ventilator care and specialist treatment.", 43, "â‚¹1,72,000", "â‚¹4,00,000"],
    ["Medicines for a senior patient", "Monthly medicines and follow-up tests.", 61, "â‚¹91,500", "â‚¹1,50,000"],
    ["Support premature baby care", "NICU bills for a newborn child.", 49, "â‚¹2,45,000", "â‚¹5,00,000"],
    ["Spine surgery assistance", "Surgery, rehabilitation, and physiotherapy.", 57, "â‚¹2,85,000", "â‚¹5,00,000"],
    ["Dialysis care for six months", "Regular dialysis and transport support.", 72, "â‚¹1,44,000", "â‚¹2,00,000"],
    ["Accident recovery treatment", "Operations and recovery expenses.", 35, "â‚¹1,05,000", "â‚¹3,00,000"],
    ["Hospital bills for mother", "Urgent inpatient care and diagnostics.", 82, "â‚¹4,10,000", "â‚¹5,00,000"],
    ["Eye surgery for school teacher", "Procedure and post-care medicines.", 64, "â‚¹96,000", "â‚¹1,50,000"],
    ["Physiotherapy after stroke", "Therapy sessions and mobility aids.", 45, "â‚¹67,500", "â‚¹1,50,000"]
  ],
  education: [
    ["Keep Anika in school", "Fees, books, and uniform support.", 71, "â‚¹71,000", "â‚¹1,00,000"],
    ["Scholarships for rural students", "Tuition help for first-generation learners.", 58, "â‚¹2,32,000", "â‚¹4,00,000"],
    ["Laptop fund for coding learners", "Devices for low-income students.", 44, "â‚¹1,10,000", "â‚¹2,50,000"],
    ["College fees for engineering student", "Semester fees and hostel costs.", 63, "â‚¹1,89,000", "â‚¹3,00,000"],
    ["Books for community library", "Study material for children.", 80, "â‚¹80,000", "â‚¹1,00,000"],
    ["Girls education support", "School fees and safe transport.", 52, "â‚¹1,56,000", "â‚¹3,00,000"],
    ["Skill training for young adults", "Course fees and certification.", 39, "â‚¹78,000", "â‚¹2,00,000"],
    ["Exam coaching assistance", "Competitive exam preparation fees.", 47, "â‚¹94,000", "â‚¹2,00,000"],
    ["Uniforms for 120 students", "School uniforms and shoes.", 66, "â‚¹1,32,000", "â‚¹2,00,000"],
    ["Science lab for local school", "Basic lab equipment and safety kits.", 42, "â‚¹2,10,000", "â‚¹5,00,000"],
    ["Support a nursing student", "Fees, books, and practical training.", 73, "â‚¹1,46,000", "â‚¹2,00,000"],
    ["Digital classroom setup", "Projector, speakers, and learning tools.", 55, "â‚¹1,65,000", "â‚¹3,00,000"]
  ],
  nonprofit: [
    ["Meals for daily wage families", "Fresh food kits for vulnerable families.", 69, "â‚¹2,07,000", "â‚¹3,00,000"],
    ["Winter blankets drive", "Warm blankets for street communities.", 86, "â‚¹1,29,000", "â‚¹1,50,000"],
    ["Clean water for village homes", "Filters and storage tanks.", 46, "â‚¹2,30,000", "â‚¹5,00,000"],
    ["Women safety workshops", "Training, travel, and resource kits.", 51, "â‚¹1,02,000", "â‚¹2,00,000"],
    ["Community health camp", "Doctor visits and basic medicines.", 63, "â‚¹1,89,000", "â‚¹3,00,000"],
    ["Food bank monthly support", "Groceries for families in crisis.", 74, "â‚¹2,96,000", "â‚¹4,00,000"],
    ["Shelter repairs before monsoon", "Roof repairs and hygiene facilities.", 37, "â‚¹1,48,000", "â‚¹4,00,000"],
    ["Assistive devices for seniors", "Wheelchairs and walking aids.", 59, "â‚¹1,77,000", "â‚¹3,00,000"],
    ["Mental health helpline", "Counselor sessions and operations.", 41, "â‚¹1,23,000", "â‚¹3,00,000"],
    ["Support local artisans", "Tools and fair market access.", 67, "â‚¹1,34,000", "â‚¹2,00,000"],
    ["Sanitation kit drive", "Hygiene kits for underserved areas.", 77, "â‚¹1,54,000", "â‚¹2,00,000"],
    ["Community learning center", "Rent, books, and volunteer support.", 48, "â‚¹2,40,000", "â‚¹5,00,000"]
  ],
  animal: [
    ["Rescue surgery for Bruno", "Emergency vet care after an accident.", 62, "â‚¹93,000", "â‚¹1,50,000"],
    ["Food for shelter animals", "Monthly food for rescued dogs and cats.", 79, "â‚¹1,58,000", "â‚¹2,00,000"],
    ["Vaccination camp for street dogs", "Vaccines, transport, and vet support.", 53, "â‚¹1,06,000", "â‚¹2,00,000"],
    ["Build safe kennels", "Monsoon-safe shelter spaces.", 40, "â‚¹2,00,000", "â‚¹5,00,000"],
    ["Animal ambulance fuel fund", "Keep rescue vehicles running.", 57, "â‚¹85,500", "â‚¹1,50,000"],
    ["Treatment for injured kitten", "Surgery, medicines, and foster care.", 88, "â‚¹44,000", "â‚¹50,000"],
    ["Sterilization drive", "Humane population control support.", 46, "â‚¹1,38,000", "â‚¹3,00,000"],
    ["Care for abandoned cattle", "Feed and medical care.", 60, "â‚¹1,20,000", "â‚¹2,00,000"],
    ["Bird rescue rehabilitation", "Safe recovery space and supplies.", 35, "â‚¹52,500", "â‚¹1,50,000"],
    ["Shelter medical supplies", "Bandages, medicines, and cleaning kits.", 64, "â‚¹96,000", "â‚¹1,50,000"],
    ["Adoption camp setup", "Tents, crates, and awareness material.", 43, "â‚¹64,500", "â‚¹1,50,000"],
    ["Winter bedding for rescues", "Warm beds for shelter animals.", 81, "â‚¹81,000", "â‚¹1,00,000"]
  ],
  emergency: [
    ["Flood relief family kits", "Food, clothes, and hygiene supplies.", 75, "â‚¹3,00,000", "â‚¹4,00,000"],
    ["Fire accident home recovery", "Temporary housing and essentials.", 48, "â‚¹1,92,000", "â‚¹4,00,000"],
    ["Road accident urgent support", "Hospital bills and family expenses.", 64, "â‚¹2,56,000", "â‚¹4,00,000"],
    ["Disaster relief medical camp", "Doctors, medicines, and transport.", 39, "â‚¹1,95,000", "â‚¹5,00,000"],
    ["Help rebuild a small shop", "Equipment and inventory after loss.", 58, "â‚¹1,74,000", "â‚¹3,00,000"],
    ["Emergency rent for family", "Short-term housing stability.", 82, "â‚¹82,000", "â‚¹1,00,000"],
    ["Cyclone relief supplies", "Tarps, food kits, and clean water.", 44, "â‚¹2,20,000", "â‚¹5,00,000"],
    ["Support after sudden job loss", "Groceries and school fee bridge.", 50, "â‚¹75,000", "â‚¹1,50,000"],
    ["Ambulance transfer fund", "Urgent patient transport.", 69, "â‚¹69,000", "â‚¹1,00,000"],
    ["Emergency documents recovery", "Legal IDs and essential records.", 36, "â‚¹36,000", "â‚¹1,00,000"],
    ["Landslide relief for families", "Food, bedding, and medicines.", 55, "â‚¹2,75,000", "â‚¹5,00,000"],
    ["Urgent support for orphaned children", "Care, food, and education bridge.", 73, "â‚¹2,19,000", "â‚¹3,00,000"]
  ]
};

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

function campaignCard(categoryKey, item) {
  const [title, copy, progress, raised, goal] = item;
  const category = categories[categoryKey];
  return `
    <article class="campaign-card" data-campaign-category="${categoryKey}" data-campaign-title="${title}">
      <div class="campaign-art" aria-hidden="true"><i class="fa-solid ${category.icon}"></i></div>
      <div class="campaign-body">
        <h3>${title}</h3>
        <p>${copy}</p>
        <div class="progress" aria-label="${progress}% funded"><span style="width:${progress}%"></span></div>
        <div class="campaign-meta"><span><b>${raised}</b> raised</span><span>${goal}</span></div>
        <button class="campaign-donate" type="button">View fundraiser</button>
      </div>
    </article>
  `;
}

function renderDonateSections() {
  const host = document.querySelector("[data-donate-sections]");
  if (!host) return;

  host.innerHTML = Object.keys(categories).map(key => {
    const category = categories[key];
    const cards = campaigns[key].slice(0, 6).map(item => campaignCard(key, item)).join("");
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
  }).join("");
}

function renderCategoryGrid() {
  const grid = document.querySelector("[data-category-grid]");
  if (!grid) return;

  const categoryKey = grid.dataset.categoryGrid;
  const category = categories[categoryKey];
  const items = campaigns[categoryKey] || [];
  const title = document.querySelector("[data-category-title]");
  const desc = document.querySelector("[data-category-desc]");

  if (title) title.textContent = `${category.title} fundraisers`;
  if (desc) desc.textContent = category.text;
  grid.innerHTML = items.map(item => campaignCard(categoryKey, item)).join("");
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

function parseCampaignMoney(value){return Number(String(value).replace(/[^0-9]/g,""))||0}
document.addEventListener("click",event=>{const card=event.target.closest(".campaign-card");if(!card||!window.Kindr)return;const key=card.dataset.campaignCategory,item=(campaigns[key]||[]).find(x=>x[0]===card.dataset.campaignTitle);if(item)window.Kindr.openCampaign({id:`featured_${key}_${item[0].toLowerCase().replace(/[^a-z0-9]+/g,"_")}`,title:item[0],category:categories[key].title,story:item[1],raised:parseCampaignMoney(item[3]),goal:parseCampaignMoney(item[4]),city:"India",image:`hero${(campaigns[key].indexOf(item)%5)+1}.jpg`})});
const kindrStyle=document.createElement("link");kindrStyle.rel="stylesheet";kindrStyle.href="app.css";document.head.append(kindrStyle);if(!window.Kindr){const kindrScript=document.createElement("script");kindrScript.src="app.js";document.body.append(kindrScript)}



function backendCampaignCard(campaign) {
  const pct = Math.min(100, Math.round((Number(campaign.raised) / Number(campaign.goal)) * 100) || 0);
  const clean = value => String(value || "").replace(/[&<>"']/g, char => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[char]));
  return `<article class="campaign-card live-campaign-card">
    <img class="campaign-art campaign-photo" src="${clean(campaign.image || "hero1.jpg")}" alt="${clean(campaign.title)}">
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
