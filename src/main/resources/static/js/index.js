'use strict';

function slugInvalid(errorMessage) {
    document.getElementById('slug-error-message').innerText = errorMessage;
    document.getElementById('slug').classList.add('is-invalid');
}

function slugValid() {
    document.getElementById('slug-error-message').innerText = '';
    document.getElementById('slug').classList.remove('is-invalid');
}

function ajaxCreateRecord() {
    var slug = document.getElementById('slug').value;
    var targetUrl = document.getElementById('target-url').value;
    var duration = document.getElementById('expire-duration').value;
    var request = {
        slug: slug,
        targetUrl: targetUrl,
        expireDuration: duration
    };
    fetch('/api/v1/records', {
        method: 'POST',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(request)
    }).then(response => response.json())
        .then(data => {
            if (data.success) {
                var successUrlDom = document.getElementById('success-url');
                successUrlDom.href = data.url;
                successUrlDom.innerText = data.url;
                document.getElementById('success-panel').classList.remove('d-none');
            } else {
                document.getElementById('slug').classList.add('is-invalid');
                document.getElementById('success-panel').classList.add('d-none');
            }
        })
        .catch(err => console.log(err));
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
        })
        .catch(err => console.log(err));
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
    if (slug.indexOf('?') >= 0) {
        slugInvalid('Cannot contains special character: ?')
        throttledAjaxCheckSlug.cancel();
        return false;
    }

    var restrictedSegments = ['api', 'css', 'js'];
    for (var segment in restrictedSegments) {
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

    ajaxCreateRecord();
}

function handleSlugInput() {
    var valid = validateSlug();
    if (!valid) return;

    throttledAjaxCheckSlug();
}
