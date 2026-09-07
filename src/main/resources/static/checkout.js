(async function () {

    const host = document.querySelector('[data-checkout]');

    await Kindr.getMe();

    const me = Kindr.currentUser();

    const draft = JSON.parse(
        sessionStorage.getItem('kindr_checkout') || 'null'
    );

    // =========================================================
    // 1. CHECK LOGIN
    // =========================================================

    if (!me) {
        location.replace(
            `sign-in.html?next=${encodeURIComponent('checkout.html')}`
        );
        return;
    }

    // =========================================================
    // 2. CHECK CHECKOUT DATA
    // =========================================================

    if (!draft || !draft.campaignId || !draft.amount) {

        host.innerHTML = `
            <div class="checkout-success">
                <h1>Your checkout has expired</h1>
                <p>
                    Return to a campaign and choose a donation amount.
                </p>
                <a href="donate.html">
                    Explore fundraisers
                </a>
            </div>
        `;

        return;
    }

    let detail;
    let config;

    // =========================================================
    // 3. LOAD CAMPAIGN + PAYMENT CONFIG
    // =========================================================

    try {

        [detail, config] = await Promise.all([

            Kindr.api(
                `/api/campaigns/${encodeURIComponent(draft.campaignId)}`
            ),

            Kindr.api(
                '/api/payments/config'
            )

        ]);

    } catch (e) {

        host.innerHTML = `
            <div class="checkout-success">

                <h1>Checkout unavailable</h1>

                <p>
                    ${Kindr.escape(e.message)}
                </p>

                <a href="donate.html">
                    Explore fundraisers
                </a>

            </div>
        `;

        return;
    }

    const c = detail.campaign;

    const fee = 0;

    const total = Number(draft.amount);

    // prefer API-served image when campaign stores image bytes in DB
    const imageSrc = (c && c.imageContentType) ? `/api/campaigns/${encodeURIComponent(c.id)}/image` : (c.image || 'hero1.jpg');

    // =========================================================
    // 4. DISPLAY CHECKOUT
    // =========================================================

    host.innerHTML = `

        <div class="checkout-grid">

            <section class="checkout-card">

                <span class="eyebrow">
                    Complete donation
                </span>

                <h1>
                    Choose how to pay
                </h1>

                <p>
                    Your payment details are handled by the secure gateway.
                </p>

                ${
                    config.mode === 'test'
                        ? `
                            <div class="test-banner">
                                <strong>Test mode:</strong>
                                no real money will be charged.
                                Add Razorpay environment keys to enable
                                live checkout.
                            </div>
                          `
                        : ''
                }

                <form data-pay-form>

                    <div class="pay-methods">

                        <label class="pay-method">

                            <input
                                type="radio"
                                name="method"
                                value="upi"
                                checked
                            >

                            <strong>UPI</strong>

                            <small>
                                GPay, PhonePe
                            </small>

                        </label>


                        <label class="pay-method">

                            <input
                                type="radio"
                                name="method"
                                value="card"
                            >

                            <strong>Card</strong>

                            <small>
                                Debit or credit
                            </small>

                        </label>


                        <label class="pay-method">

                            <input
                                type="radio"
                                name="method"
                                value="netbanking"
                            >

                            <strong>Net banking</strong>

                            <small>
                                All major banks
                            </small>

                        </label>

                    </div>


                    <div
                        class="gateway-fields"
                        data-test-fields
                    >

                        ${
                            config.mode === 'test'
                                ? `
                                    <input
                                        name="payer"
                                        placeholder="Name on payment"
                                        value="${Kindr.escape(
                                            me.firstName + ' ' + me.lastName
                                        )}"
                                        required
                                    >

                                    <div class="field-row">

                                        <input
                                            value="4242 4242 4242 4242"
                                            aria-label="Test card number"
                                            readonly
                                        >

                                        <input
                                            value="Any future date / CVV"
                                            aria-label="Test card details"
                                            readonly
                                        >

                                    </div>
                                  `
                                : ''
                        }

                    </div>


                    <button class="pay-button">
                        Pay ${Kindr.formatMoney(total)}
                    </button>

                </form>

            </section>


            <aside class="checkout-summary">

                <img
                    src="${Kindr.escape(imageSrc)}"
                    alt=""
                >

                <h2>
                    ${Kindr.escape(c.title)}
                </h2>


                <div class="summary-line">

                    <span>Donation</span>

                    <strong>
                        ${Kindr.formatMoney(total)}
                    </strong>

                </div>


                <div class="summary-line">

                    <span>Platform fee</span>

                    <strong>
                        ${Kindr.formatMoney(fee)}
                    </strong>

                </div>


                <div class="summary-line total">

                    <span>Total</span>

                    <strong>
                        ${Kindr.formatMoney(total)}
                    </strong>

                </div>


                <div class="trust-list">
                    ✓ Encrypted checkout
                    <br>
                    ✓ Donation recorded after verification
                    <br>
                    ✓ Activity saved to your profile
                </div>

            </aside>

        </div>
    `;


    const form =
        host.querySelector('[data-pay-form]');


    // =========================================================
    // 5. PAY BUTTON
    // =========================================================

    form.addEventListener(
        'submit',
        async e => {

            e.preventDefault();

            const btn =
                form.querySelector('button');

            btn.disabled = true;

            btn.textContent =
                'Creating secure order…';


            try {

                const method =
                    new FormData(form)
                        .get('method');


                // =================================================
                // CREATE PAYMENT ORDER
                // =================================================

                const order =
                    await Kindr.api(
                        '/api/payments/order',
                        {
                            method: 'POST',

                            body: JSON.stringify({
                                ...draft,
                                method
                            })
                        }
                    );


                console.log(
                    "Payment order created:",
                    order
                );


                // =================================================
                // RAZORPAY MODE
                // =================================================

                if (
                    order.payment.mode ===
                    'razorpay'
                ) {

                    if (!window.Razorpay) {

                        throw new Error(
                            'Payment gateway could not load.'
                        );
                    }


                    const razor =
                        new Razorpay({

                            key:
                                order.keyId,

                            amount:
                                Math.round(
                                    total * 100
                                ),

                            currency:
                                'INR',

                            name:
                                'Kindr',

                            description:
                                c.title,

                            order_id:
                                order.payment
                                    .providerOrderId,


                            prefill: {

                                name:
                                    `${me.firstName} ${me.lastName}`,

                                email:
                                    me.email,

                                contact:
                                    me.phone ||
                                    me.mobileNumber ||
                                    ''

                            },


                            theme: {

                                color:
                                    '#0f766e'

                            },


                            // =====================================
                            // RAZORPAY PAYMENT SUCCESS
                            // =====================================

                            handler:
                                async function (response) {

                                    console.log(
                                        "========== RAZORPAY SUCCESS =========="
                                    );

                                    console.log(
                                        "Internal Payment ID:",
                                        order.payment.id
                                    );

                                    console.log(
                                        "Razorpay Payment ID:",
                                        response.razorpay_payment_id
                                    );

                                    console.log(
                                        "Razorpay Order ID:",
                                        response.razorpay_order_id
                                    );

                                    console.log(
                                        "Razorpay Signature:",
                                        response.razorpay_signature
                                    );

                                    console.log(
                                        "Full Razorpay Response:",
                                        response
                                    );

                                    console.log(
                                        "======================================"
                                    );


                                    try {

                                        await confirm(
                                            order.payment.id,
                                            response
                                        );


                                        console.log(
                                            "Backend confirmation SUCCESS"
                                        );


                                    } catch (error) {

                                        console.error(
                                            "Backend confirmation FAILED:",
                                            error
                                        );


                                        btn.disabled =
                                            false;

                                        btn.textContent =
                                            `Pay ${Kindr.formatMoney(total)}`;


                                        Kindr.toast(
                                            error.message ||
                                            'Payment succeeded but backend confirmation failed.',
                                            'error'
                                        );

                                    }

                                }

                        });


                    // =============================================
                    // RAZORPAY PAYMENT FAILURE
                    // =============================================

                    razor.on(
                        'payment.failed',
                        function (response) {

                            console.error(
                                "Razorpay payment failed:",
                                response
                            );


                            btn.disabled =
                                false;


                            btn.textContent =
                                `Pay ${Kindr.formatMoney(total)}`;


                            Kindr.toast(
                                response.error?.description ||
                                'Payment failed.',
                                'error'
                            );

                        }
                    );


                    // =============================================
                    // OPEN RAZORPAY
                    // =============================================

                    console.log(
                        "Opening Razorpay Checkout..."
                    );


                    razor.open();

                }

                // =================================================
                // LOCAL TEST MODE
                // =================================================

                else {

                    await confirm(
                        order.payment.id,
                        {
                            testResult:
                                'success'
                        }
                    );

                }


            } catch (err) {

                console.error(
                    "Checkout error:",
                    err
                );


                btn.disabled =
                    false;


                btn.textContent =
                    `Pay ${Kindr.formatMoney(total)}`;


                Kindr.toast(
                    err.message,
                    'error'
                );

            }

        }
    );


    // =========================================================
    // 6. CONFIRM PAYMENT WITH BACKEND
    // =========================================================

    async function confirm(
        paymentId,
        response
    ) {

        console.log(
            "Sending payment confirmation to backend..."
        );


        const confirmRequest = {

            paymentId:
                paymentId,

            ...response

        };


        console.log(
            "Confirm request:",
            confirmRequest
        );


        const result =
            await Kindr.api(
                '/api/payments/confirm',
                {

                    method:
                        'POST',

                    body:
                        JSON.stringify(
                            confirmRequest
                        )

                }
            );


        console.log(
            "Confirm response:",
            result
        );


        // IMPORTANT:
        // Remove checkout only AFTER
        // backend confirmation succeeds.

        sessionStorage.removeItem(
            'kindr_checkout'
        );


        host.innerHTML = `

            <div class="checkout-success">

                <div class="success-icon">
                    ✓
                </div>

                <h1>
                    Thank you for your donation!
                </h1>

                <p>

                    ${Kindr.formatMoney(
                        result.donation.amount
                    )}

                    was donated to

                    <strong>
                        ${Kindr.escape(
                            result.donation.campaignTitle
                        )}
                    </strong>.

                </p>

                <p>

                    Receipt ID:

                    ${Kindr.escape(
                        result.donation.id
                    )}

                </p>

                <a href="profile.html#donations">
                    View donation activity
                </a>

            </div>
        `;

    }

})();