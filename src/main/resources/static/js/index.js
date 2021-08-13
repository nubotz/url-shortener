'use strict';

window.submitting = false;

function slugInvalid(errorMessage) {
    document.getElementById('slug-error-message').innerText = errorMessage;
    document.getElementById('slug').classList.add('is-invalid');
}

function slugValid() {
    document.getElementById('slug-error-message').innerText = '';
    document.getElementById('slug').classList.remove('is-invalid');
}

function targetUrlInvalid(errorMessage) {
    document.getElementById('target-url-error-message').innerText = errorMessage;
    document.getElementById('target-url').classList.add('is-invalid');
}

function targetUrlValid() {
    document.getElementById('target-url-error-message').innerText = '';
    document.getElementById('target-url').classList.remove('is-invalid');
}

function ajaxCreateRecord() {
    if (window.submitting) return;

    var slug = document.getElementById('slug').value;
    var targetUrl = document.getElementById('target-url').value;
    var duration = document.getElementById('expire-duration').value;
    var request = {
        slug: slug.trim(),
        targetUrl: targetUrl.trim(),
        expireDuration: duration
    };
    window.submitting = true;
    fetch('/api/v1/records', {
        method: 'POST',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(request)
    }).then(response => {
        window.submitting = false;
        return response.json();
    }).then(data => {
        if (data.success) {
            var successUrlDom = document.getElementById('success-url');
            successUrlDom.href = data.url;
            successUrlDom.innerText = data.url;
            document.getElementById('success-panel').classList.remove('d-none');
        } else {
            slugInvalid(data.errorMessage);
            document.getElementById('success-panel').classList.add('d-none');
        }
    }).catch(err => {
        window.submitting = false;
        console.log(err)
    });
}

function ajaxCheckSlug() {
    var slugDom = document.getElementById('slug');
    var slug = slugDom.value;
    var request = {slug: slug};
    fetch('/api/v1/records/check-slug', {
        method: 'PUT',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(request)
    }).then(response => response.json())
        .then(data => {
            if (data.success) {
                slugValid();
            } else {
                slugInvalid('Slug has been taken');
            }
        }).catch(err => console.log(err));
}

var throttledAjaxCheckSlug = _.throttle(ajaxCheckSlug, 500);


function validateSlug() {
    var slugDom = document.getElementById('slug');
    var slug = slugDom.value;

    if (slug.length < 3) {
        slugInvalid('At least 3 characters long');
        throttledAjaxCheckSlug.cancel();
        return false;
    }

    for (var c of ['?', '/', '#']) {
        if (slug.indexOf(c) >= 0) {
            slugInvalid('Cannot contains special character: ?, /, #');
            throttledAjaxCheckSlug.cancel();
            return false;
        }
    }
    for (var segment of ['api', 'css', 'js']) {
        if (slug.indexOf(segment) === 0) {
            slugInvalid('Cannot starts with reserved prefix: api, css, js');
            throttledAjaxCheckSlug.cancel();
            return false;
        }
    }
    return true;
}

function submit() {
    var valid = validateSlug();
    if (!valid) return;

    // validateTargetUrl
    if (document.getElementById('target-url').value.trim()) {
        targetUrlValid();
    } else {
        targetUrlInvalid('Cannot be empty');
        return;
    }

    ajaxCreateRecord();
}

function handleSlugInput() {
    var valid = validateSlug();
    if (!valid) return;

    throttledAjaxCheckSlug();
}
