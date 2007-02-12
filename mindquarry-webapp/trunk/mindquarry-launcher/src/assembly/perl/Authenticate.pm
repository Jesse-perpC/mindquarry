package Mindquarry::Authenticate;

use strict;
use warnings;

use LWP::UserAgent;
use Apache2::Const -compile => qw(OK DECLINED HTTP_UNAUTHORIZED);
use Apache2::RequestRec;
use Apache2::ServerUtil ();
use Apache2::Access ();
use Apache2::RequestUtil ();
use Apache2::Log ();

sub handler {
	my $r = shift;
	my $base = $r->dir_config('AuthBase');
	my $s = Apache2::ServerUtil->server;
	# Let subrequests pass.
	#return Apache2::Const::DECLINED unless $r->is_initial_req;
	
	# Get the client-supplied credentials.
	my ($status, $password) = $r->get_basic_auth_pw;
	#return $status unless $status == Apache2::Const::OK;
	# Perform some custom user/password validation.
	my $pathinfo = $r->path_info;
	$pathinfo =~ s/\/([^\/]*)\/(.*)/\/$1\//;
	return Apache2::Const::OK if authenticate($r->user, $password, $base . $pathinfo);
	# Whoops, bad credentials.
	$r->note_basic_auth_failure;
	return Apache2::Const::HTTP_UNAUTHORIZED;
}

sub authenticate {
	my ($username, $password, $url) = @_;
	my $agent = LWP::UserAgent->new("Mindquarry Authentication Handler");
	my $override = sprintf '%s::get_basic_credentials', ref $agent;
	my $response;
	my $req;

	my $s = Apache2::ServerUtil->server;
	$s->log_error("AuthURL: " . $url . " User: " . $username);
	
	no strict   'refs';
        no warnings 'redefine';
	
	local *$override = sub {
            return ( $username, $password );
        };
	#$agent->request_redirectable() = {};	
	#$agent->default_header('Accept' => "text/plain");


	$url = $url."?request=login&targetUri=/";
	$s->log_error("URL: ".$url);
	#$response = $agent->head($url, 'Accept' => 'text/plain');
	
	$req = HTTP::Request->new(HEAD => $url);
	$req->header('Accept' => 'text/xml');

	$response = $agent->request($req);


	$s->log_error("Response Code: ".$response->code);	
	#$s->log_error("Response: ".$response->as_string);
	if ($response->code == 401) {
		$s->log_error("HTTP Code 401");
		return 0;
	}
	if ($response->is_error) {
		$s->log_error("Error Message: " . $response->status_line);
		return 0;
	}
	$s->log_error("Authentication seems ok");
	return 1;
}

1;
